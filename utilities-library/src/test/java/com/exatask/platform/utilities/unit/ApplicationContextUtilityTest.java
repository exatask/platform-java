package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.ApplicationContextUtility;
import com.exatask.platform.utilities.errors.AppError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApplicationContextUtilityTest {

  @Mock
  private ApplicationContext applicationContext;

  @InjectMocks
  private ApplicationContextUtility applicationContextUtility;

  @BeforeEach
  public void setUp() {

    applicationContextUtility.setApplicationContext(applicationContext);

    Mockito.when(applicationContext.getBean("AppError"))
        .thenReturn(null);
    Mockito.when(applicationContext.getBean(AppError.class))
        .thenReturn(null);
  }

  @Test
  public void shouldReturnNull_getBean() {

    Assertions.assertNull(ApplicationContextUtility.getBean("AppError"));
    Assertions.assertNull(ApplicationContextUtility.getBean(AppError.class));
  }
}
