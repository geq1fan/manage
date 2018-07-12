package com.ge.modules.schedule.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleService;

@Transactional
@Service
public class ScheduleServiceImpl extends BaseServiceImpl<ScheduleInfo> implements ScheduleService {

}
