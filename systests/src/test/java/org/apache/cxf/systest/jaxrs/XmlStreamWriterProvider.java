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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|XMLStreamWriter
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
name|jaxrs
operator|.
name|ext
operator|.
name|ResponseHandler
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
name|jaxrs
operator|.
name|model
operator|.
name|OperationResourceInfo
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|XmlStreamWriterProvider
implements|implements
name|ResponseHandler
block|{
specifier|public
name|Response
name|handleResponse
parameter_list|(
name|Message
name|m
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|String
name|method
init|=
name|ori
operator|.
name|getHttpMethod
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"POST"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
operator|new
name|CustomXmlStreamWriter
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

