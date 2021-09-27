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

package org.apache.rocketmq.client.common;

import org.apache.rocketmq.client.latency.MQFaultStrategy;

import java.util.Random;

public class ThreadLocalIndex {
    private final ThreadLocal<Integer> threadLocalIndex = new ThreadLocal<Integer>();
    private final Random random = new Random();

    /**
     * 随机选择一个，然后开始递增，线程所有，啥意思呢，就是发送消息时，随机选择一个通道，递增取模就是从当前开始，往下循环找（根据重试次数）
     * @see MQFaultStrategy#selectOneMessageQueue(org.apache.rocketmq.client.impl.producer.TopicPublishInfo, java.lang.String)
     * @return
     */

    public int incrementAndGet() {
        Integer index = this.threadLocalIndex.get();
        if (null == index) {
            index = Math.abs(random.nextInt());
            this.threadLocalIndex.set(index);
        }

        this.threadLocalIndex.set(++index);
        return Math.abs(index);
    }

    @Override
    public String toString() {
        return "ThreadLocalIndex{" +
            "threadLocalIndex=" + threadLocalIndex.get() +
            '}';
    }
}
