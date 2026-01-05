package com.junwoo.axonstudy.service;

import com.junwoo.axonstudy.command.CreateElephantCommand;
import com.junwoo.axonstudy.command.EnterElephantCommand;
import com.junwoo.axonstudy.command.ExitElephantCommand;
import com.junwoo.axonstudy.dto.ElephantDTO;
import com.junwoo.axonstudy.dto.StatusEnum;
import com.junwoo.axonstudy.entity.Elephant;
import com.junwoo.axonstudy.queries.GetElephantQuery;
import com.junwoo.axonstudy.repository.ElephantRepository;
import com.junwoo.axonstudy.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 5.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElephantService {

    private final ElephantRepository elephantRepository;
    private final transient CommandGateway commandGateway;
    private final transient QueryGateway queryGateway;

    public ResultVO<CreateElephantCommand> create(ElephantDTO elephant) {
        log.info("[ElephantService] Executing create: {}", elephant.toString());

        ResultVO<CreateElephantCommand> retVO = new ResultVO<>();

        // check validation
        if (elephant.getWeight() < 30 || elephant.getWeight() > 200) {
            retVO.setResultCode(false);
            retVO.setReturnMessage("몸무게는 30kg 이상 200kg 이하로 입력하세요");
            return retVO;
        }

        // send command
        CreateElephantCommand cmd = CreateElephantCommand.builder()
                .id(RandomStringUtils.randomAlphanumeric(3))
                .name(elephant.getName())
                .weight(elephant.getWeight())
                .status(StatusEnum.READY.value())
                .build();

        try {
            commandGateway.sendAndWait(cmd, 30, TimeUnit.SECONDS);
            retVO.setResultCode(true);
            retVO.setReturnMessage("Success to create elephant");
            retVO.setResult(cmd);
        } catch (Exception e) {
            retVO.setResultCode(false);
            retVO.setReturnMessage(e.getMessage());
        }

        return retVO;
    }

    public ResultVO<String> enter(String id) {
        log.info("[ElephantService] Executing enter for Id: {}", id);

        ResultVO<String> retVO = new ResultVO<>();

        // check validation
        Elephant elephant = getEntity(id);
        if (elephant.getStatus().equals(StatusEnum.ENTER.value())) {
            retVO.setResultCode(false);
            retVO.setReturnMessage("이미 냉장고 안에 있는 코끼리입니다");
            return retVO;
        }

        // send command
        try {
            commandGateway.sendAndWait(EnterElephantCommand.builder()
                    .id(id)
                    .status(StatusEnum.ENTER.value())
                    .build(), 30, TimeUnit.SECONDS);

            retVO.setResultCode(true);
            retVO.setReturnMessage("Success to request enter elephant");
        } catch (Exception e) {
            retVO.setResultCode(false);
            retVO.setReturnMessage(e.getMessage());
        }

        return retVO;
    }

    public ResultVO<String> exit(String id) {
        log.info("[ElephantService] Executing exit for Id: {}", id);

        ResultVO<String> retVO = new ResultVO<>();

        // check validation
        Elephant elephant = getEntity(id);
        if (!elephant.getStatus().equals(StatusEnum.ENTER.value())) {
            retVO.setResultCode(false);
            retVO.setReturnMessage("냉장고 안에 있는 코끼리만 꺼낼 수 있습니다");
            return retVO;
        }

        // send command
        try {
            commandGateway.sendAndWait(ExitElephantCommand.builder()
                    .id(id)
                    .status(StatusEnum.EXIT.value())
                    .build(), 30, TimeUnit.SECONDS);

            retVO.setResultCode(true);
            retVO.setReturnMessage("Success to request exit elephant");
        } catch (Exception e) {
            retVO.setResultCode(false);
            retVO.setReturnMessage(e.getMessage());
        }

        return retVO;
    }

    public ResultVO<Elephant> getElephant(String id) {
        log.info("[ElephantService] Executing getElephant for Id: {}", id);

        ResultVO<Elephant> retVO = new ResultVO<>();
        Elephant elephant = queryGateway.query(new GetElephantQuery(id), ResponseTypes.instanceOf(Elephant.class)).join();

        if (elephant != null) {
            retVO.setResultCode(true);
            retVO.setReturnMessage("ID: " + id);
            retVO.setResult(elephant);
        } else {
            retVO.setResultCode(false);
            retVO.setReturnMessage("Can't get elephant for Id: " + id);
        }

        return retVO;
    }

    public ResultVO<List<Elephant>> getLists() {
        log.info("[ElephantService] Executing getLists");

        ResultVO<List<Elephant>> retVO = new ResultVO<>();

        List<Elephant> elephants = queryGateway.query("list", "", ResponseTypes.multipleInstancesOf(Elephant.class)).join();

        retVO.setResultCode(true);
        retVO.setReturnMessage("코끼리수: " + elephants.size());
        retVO.setResult(elephants);

        return retVO;
    }

    private Elephant getEntity(String id) {
        Optional<Elephant> optionalElephant = elephantRepository.findById(id);

        return optionalElephant.orElse(null);
    }

    @QueryHandler
    private Elephant getElephant(GetElephantQuery query) {
        Optional<Elephant> optionalElephant = elephantRepository.findById(query.getId());

        return optionalElephant.orElse(null);
    }
}
