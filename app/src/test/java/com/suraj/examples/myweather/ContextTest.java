package com.suraj.examples.myweather;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;

import java.io.FileOutputStream;
import java.io.IOException;

import service.Utils;

/**
 * Created by surajbhattarai on 9/11/15.
 */
public class ContextTest {

    @Mock
    Context context;

    @Mock
    FileOutputStream fileOutputStream;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    //verify openFileOutput is called exactly once
    //the write() method is called atLeast/exactly twice.

    @Test
    public void writeShouldWriteTwiceToFileSystem() {
        try {
            when(context.openFileOutput(anyString(), anyInt())).thenReturn(fileOutputStream);
            Utils.writeConfiguration(context);
            verify(context, times(1)).openFileOutput(anyString(), anyInt());
            //verify(fileOutputStream, times(2)).write(any(byte[].class));
            verify(fileOutputStream, atLeast(2)).write(any(byte[].class));
        } catch (IOException e) {
            fail();
        }
    }
}
