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
name|systest
operator|.
name|versioning
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|stream
operator|.
name|XMLInputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|endpoint
operator|.
name|Endpoint
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
name|helpers
operator|.
name|IOUtils
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
name|interceptor
operator|.
name|Fault
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
name|interceptor
operator|.
name|StaxInInterceptor
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
name|io
operator|.
name|CachedOutputStream
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
name|phase
operator|.
name|Phase
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
name|staxutils
operator|.
name|StaxUtils
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
name|wsdl
operator|.
name|interceptors
operator|.
name|AbstractEndpointSelectionInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|MediatorInInterceptor
extends|extends
name|AbstractEndpointSelectionInterceptor
block|{
specifier|public
name|MediatorInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Endpoint
name|selectEndpoint
parameter_list|(
name|Message
name|message
parameter_list|,
name|Set
argument_list|<
name|Endpoint
argument_list|>
name|eps
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|//cache the input stream
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|(
literal|4096
argument_list|)
decl_stmt|;
try|try
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|encoding
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xsr
decl_stmt|;
name|XMLInputFactory
name|factory
init|=
name|StaxInInterceptor
operator|.
name|getXMLInputFactory
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|xsr
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
synchronized|synchronized
init|(
name|factory
init|)
block|{
name|xsr
operator|=
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|bos
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
comment|// move to the soap body
while|while
condition|(
literal|true
condition|)
block|{
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"Body"
operator|.
name|equals
argument_list|(
name|xsr
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|xsr
operator|.
name|nextTag
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|xsr
operator|.
name|isStartElement
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|methodName
init|=
name|xsr
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
comment|//if the incoming message has a element containes "SayHi", we redirect the message
comment|//to the new version of service on endpoint "local://localhost:9027/SoapContext/version2/SoapPort"
for|for
control|(
name|Endpoint
name|ep
range|:
name|eps
control|)
block|{
if|if
condition|(
name|methodName
operator|.
name|indexOf
argument_list|(
literal|"sayHi"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
literal|"2"
operator|.
name|equals
argument_list|(
name|ep
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|ep
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"1"
operator|.
name|equals
argument_list|(
name|ep
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|ep
return|;
block|}
block|}
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

