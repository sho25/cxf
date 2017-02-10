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
name|ext
operator|.
name|logging
operator|.
name|slf4j
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEvent
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
name|ext
operator|.
name|logging
operator|.
name|event
operator|.
name|LogEventSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|MDC
import|;
end_import

begin_class
specifier|public
class|class
name|Slf4jEventSender
implements|implements
name|LogEventSender
block|{
specifier|private
specifier|final
name|String
name|logCategory
decl_stmt|;
specifier|public
name|Slf4jEventSender
parameter_list|(
name|String
name|logCategory
parameter_list|)
block|{
name|this
operator|.
name|logCategory
operator|=
name|logCategory
expr_stmt|;
block|}
specifier|public
name|Slf4jEventSender
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|send
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
name|String
name|cat
init|=
name|logCategory
operator|!=
literal|null
condition|?
name|logCategory
else|:
literal|"org.apache.cxf.services."
operator|+
name|event
operator|.
name|getPortTypeName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"."
operator|+
name|event
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|cat
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|put
argument_list|(
name|keys
argument_list|,
literal|"type"
argument_list|,
name|event
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"address"
argument_list|,
name|event
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"content-type"
argument_list|,
name|event
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"encoding"
argument_list|,
name|event
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"exchangeId"
argument_list|,
name|event
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"httpMethod"
argument_list|,
name|event
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"messageId"
argument_list|,
name|event
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"responseCode"
argument_list|,
name|event
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"serviceName"
argument_list|,
name|localPart
argument_list|(
name|event
operator|.
name|getServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"portName"
argument_list|,
name|localPart
argument_list|(
name|event
operator|.
name|getPortName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|keys
argument_list|,
literal|"portTypeName"
argument_list|,
name|localPart
argument_list|(
name|event
operator|.
name|getPortTypeName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|getFullContentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|put
argument_list|(
name|keys
argument_list|,
literal|"fullContentFile"
argument_list|,
name|event
operator|.
name|getFullContentFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|put
argument_list|(
name|keys
argument_list|,
literal|"headers"
argument_list|,
name|event
operator|.
name|getHeaders
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|getLogMessage
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|localPart
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
name|name
operator|==
literal|null
condition|?
literal|null
else|:
name|name
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
specifier|private
name|String
name|getLogMessage
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|.
name|getPayload
argument_list|()
return|;
block|}
specifier|private
name|void
name|put
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

