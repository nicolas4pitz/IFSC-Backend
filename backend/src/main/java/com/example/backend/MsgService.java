package com.example.backend;

import org.springframework.stereotype.Service;

@Service
public class MsgService {

    public MsgService(MsgService msgService){
        msgService = msgService;
    }

}
