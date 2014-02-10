begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|JMSListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ThrottlingCounterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testThrottleWithJmsStartAndStop
parameter_list|()
block|{
name|JMSListenerContainer
name|listenerContainer
init|=
operator|new
name|DummyJMSListener
argument_list|()
decl_stmt|;
name|ThrottlingCounter
name|counter
init|=
operator|new
name|ThrottlingCounter
argument_list|(
name|listenerContainer
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|listenerContainer
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|listenerContainer
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|counter
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|listenerContainer
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
class|class
name|DummyJMSListener
implements|implements
name|JMSListenerContainer
block|{
name|boolean
name|running
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|running
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
name|running
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{         }
block|}
block|}
end_class

end_unit

