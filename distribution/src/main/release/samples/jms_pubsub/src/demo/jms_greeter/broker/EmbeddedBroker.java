begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jms_greeter
operator|.
name|broker
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|store
operator|.
name|memory
operator|.
name|MemoryPersistenceAdapter
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|EmbeddedBroker
block|{
specifier|private
name|EmbeddedBroker
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|BrokerService
name|broker
init|=
operator|new
name|BrokerService
argument_list|()
decl_stmt|;
name|broker
operator|.
name|setPersistenceAdapter
argument_list|(
operator|new
name|MemoryPersistenceAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setDataDirectory
argument_list|(
literal|"target/activemq_data"
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
literal|"tcp://localhost:61616"
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS broker ready ..."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|125
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JMS broker exiting"
argument_list|)
expr_stmt|;
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

