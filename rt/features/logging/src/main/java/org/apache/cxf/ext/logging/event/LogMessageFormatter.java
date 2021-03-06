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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Formats a log message showing the most important meta data  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|LogMessageFormatter
block|{
specifier|private
name|LogMessageFormatter
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|format
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
comment|// Start from the next line to have the output well-aligned
name|b
operator|.
name|append
argument_list|(
name|event
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|b
argument_list|,
literal|"Address"
argument_list|,
name|event
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|b
argument_list|,
literal|"HttpMethod"
argument_list|,
name|event
operator|.
name|getHttpMethod
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|b
argument_list|,
literal|"Content-Type"
argument_list|,
name|event
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|b
argument_list|,
literal|"ResponseCode"
argument_list|,
name|event
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|write
argument_list|(
name|b
argument_list|,
literal|"ExchangeId"
argument_list|,
name|event
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|.
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|write
argument_list|(
name|b
argument_list|,
literal|"ServiceName"
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
name|write
argument_list|(
name|b
argument_list|,
literal|"PortName"
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
name|write
argument_list|(
name|b
argument_list|,
literal|"PortTypeName"
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
block|}
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
name|write
argument_list|(
name|b
argument_list|,
literal|"FullContentFile"
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
name|write
argument_list|(
name|b
argument_list|,
literal|"Headers"
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
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|event
operator|.
name|getPayload
argument_list|()
argument_list|)
condition|)
block|{
name|write
argument_list|(
name|b
argument_list|,
literal|"Payload"
argument_list|,
name|event
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
specifier|static
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
specifier|static
name|void
name|write
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
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

