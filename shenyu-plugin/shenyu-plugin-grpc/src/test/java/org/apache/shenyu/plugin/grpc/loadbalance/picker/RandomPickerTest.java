/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.grpc.loadbalance.picker;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.LoadBalancer;
import org.apache.shenyu.plugin.grpc.loadbalance.SubChannelCopy;
import org.apache.shenyu.plugin.grpc.loadbalance.SubChannels;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The Test Case For {@link RandomPicker}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomPickerTest {

    private RandomPicker randomPicker;
    
    @Before
    public void setUp() {
        Attributes attributes = SubChannels.createAttributes(1, "ok");
        LoadBalancer.Subchannel subchannel =
                SubChannels.createSubChannel(new UnitTestReadHelper(), mock(EquivalentAddressGroup.class), attributes);
        List<LoadBalancer.Subchannel> list = new LinkedList<>();
        list.add(subchannel);
        randomPicker = new RandomPicker(list);
    }

    @Test
    public void testPickSubchannel() {
        Assert.assertNotNull(randomPicker.pickSubchannel(null));
    }

    @Test
    public void testIsEquivalentTo() {
        Assert.assertTrue(randomPicker.isEquivalentTo(randomPicker));
    }

    @Test
    public void testGetSubchannelsInfo() {
        Assert.assertNotNull(randomPicker.getSubchannelsInfo());
    }

    @Test
    public void testPick() {
        SubChannelCopy firstSubChannelCopy = mock(SubChannelCopy.class);
        SubChannelCopy secondSubChannelCopy = mock(SubChannelCopy.class);
        when(secondSubChannelCopy.getWeight()).thenReturn(10);
        List<SubChannelCopy> list = Arrays.asList(firstSubChannelCopy, secondSubChannelCopy);
        Assert.assertNotNull(randomPicker.pick(list));
        Assert.assertEquals(firstSubChannelCopy, randomPicker.pick(Collections.singletonList(firstSubChannelCopy)));
        Assert.assertEquals(firstSubChannelCopy, randomPicker.pick(Arrays.asList(firstSubChannelCopy, firstSubChannelCopy)));
        Assert.assertNull(randomPicker.pick(null));
    }
}
