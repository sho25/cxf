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
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|ContinuationProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|ContinuationWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

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
name|MessageObserver
import|;
end_import

begin_class
specifier|public
class|class
name|JMSContinuationProvider
implements|implements
name|ContinuationProvider
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|MessageObserver
name|incomingObserver
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JMSContinuationWrapper
argument_list|>
name|continuations
decl_stmt|;
specifier|public
name|JMSContinuationProvider
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Message
name|m
parameter_list|,
name|MessageObserver
name|observer
parameter_list|,
name|List
argument_list|<
name|JMSContinuationWrapper
argument_list|>
name|cList
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|inMessage
operator|=
name|m
expr_stmt|;
name|incomingObserver
operator|=
name|observer
expr_stmt|;
name|continuations
operator|=
name|cList
expr_stmt|;
block|}
specifier|public
name|ContinuationWrapper
name|getContinuation
parameter_list|()
block|{
if|if
condition|(
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|JMSContinuationWrapper
name|cw
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|JMSContinuationWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cw
operator|==
literal|null
condition|)
block|{
name|cw
operator|=
operator|new
name|JMSContinuationWrapper
argument_list|(
name|bus
argument_list|,
name|inMessage
argument_list|,
name|incomingObserver
argument_list|,
name|continuations
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|JMSContinuationWrapper
operator|.
name|class
argument_list|,
name|cw
argument_list|)
expr_stmt|;
block|}
return|return
name|cw
return|;
block|}
block|}
end_class

end_unit

