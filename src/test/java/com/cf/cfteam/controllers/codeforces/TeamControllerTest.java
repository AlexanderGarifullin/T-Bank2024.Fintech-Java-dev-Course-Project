package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.services.codeforces.TeamService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;




}