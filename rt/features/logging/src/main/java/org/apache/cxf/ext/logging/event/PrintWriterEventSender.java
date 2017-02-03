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
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PrintWriterEventSender
implements|implements
name|LogEventSender
block|{
name|PrintWriter
name|writer
decl_stmt|;
specifier|public
name|PrintWriterEventSender
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
block|}
name|void
name|setPrintWriter
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|writer
operator|=
name|w
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
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
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|Instant
operator|.
name|now
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" - PrintWriterEventSender\n"
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
name|b
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
synchronized|synchronized
init|(
name|writer
init|)
block|{
name|writer
operator|.
name|print
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|event
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
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
specifier|protected
name|void
name|put
parameter_list|(
name|StringBuilder
name|b
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
name|b
operator|.
name|append
argument_list|(
literal|"    "
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

