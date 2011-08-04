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
operator|.
name|security
operator|.
name|xml
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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|MediaType
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
name|MultivaluedMap
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|logging
operator|.
name|LogUtils
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
name|jaxrs
operator|.
name|provider
operator|.
name|JAXBElementProvider
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
name|provider
operator|.
name|ProviderFactory
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
name|message
operator|.
name|MessageContentsList
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
name|AbstractPhaseInterceptor
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
name|W3CDOMStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSConfig
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXmlSecOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractXmlSecOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|public
name|AbstractXmlSecOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|WRITE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
name|Document
name|doc
init|=
name|getDomDocument
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Document
name|finalDoc
init|=
name|processDocument
argument_list|(
name|message
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|finalDoc
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|", stacktrace: "
operator|+
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|abstract
name|Document
name|processDocument
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|private
name|Object
name|getRequestBody
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
operator|||
name|objs
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Document
name|getDomDocument
parameter_list|(
name|Message
name|m
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|getRequestBody
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|Document
condition|)
block|{
return|return
operator|(
name|Document
operator|)
name|body
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|DOMSource
condition|)
block|{
return|return
call|(
name|Document
call|)
argument_list|(
operator|(
name|DOMSource
operator|)
name|body
argument_list|)
operator|.
name|getNode
argument_list|()
return|;
block|}
name|ProviderFactory
name|pf
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Object
name|providerObject
init|=
name|pf
operator|.
name|createMessageBodyWriter
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|providerObject
operator|instanceof
name|JAXBElementProvider
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|JAXBElementProvider
name|provider
init|=
operator|(
name|JAXBElementProvider
operator|)
name|providerObject
decl_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|body
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
operator|(
name|MultivaluedMap
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|getDocument
argument_list|()
return|;
block|}
block|}
end_class

end_unit

