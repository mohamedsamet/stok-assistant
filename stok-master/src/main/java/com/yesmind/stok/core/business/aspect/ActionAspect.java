package com.yesmind.stok.core.business.aspect;

import com.yesmind.stok.core.domain.data.ActionDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.WorkStation;
import com.yesmind.stok.core.port.in.action.IActionAspect;
import com.yesmind.stok.core.port.in.service.IActionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Transactional
@Aspect
@Component
@RequiredArgsConstructor
public class ActionAspect implements IActionAspect {

    private final IActionService actionService;

    @Around("execution(* com.yesmind.stok.adapter.repository.jpa.*.save(..))")
    public Object trackSaveAction(ProceedingJoinPoint joinPoint) throws Throwable {

        Object entity = joinPoint.proceed();

        if (entity instanceof Product trackable) {
            actionService.record(ActionDto.builder()
                    .actionPublicID(trackable.getPublicId())
                    .type(trackable.getActionType())
                    .description(trackable.getDescription())
                    .operator(trackable.getUser())
                    .build());
        }

        if (entity instanceof WorkStation trackable)
            actionService.manageWorkStationActions(trackable);

        return entity;
    }

}
