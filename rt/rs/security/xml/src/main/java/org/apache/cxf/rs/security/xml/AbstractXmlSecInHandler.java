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
name|rs
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|XMLStreamReader
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|NodeList
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|utils
operator|.
name|JAXRSUtils
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
name|W3CDOMStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|WSProviderConfig
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXmlSecInHandler
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|SIG_NS
init|=
literal|"http://www.w3.org/2000/09/xmldsig#"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|SIG_PREFIX
init|=
literal|"ds"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ENC_NS
init|=
literal|"http://www.w3.org/2001/04/xmlenc#"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|ENC_PREFIX
init|=
literal|"xenc"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|WSU_NS
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
decl_stmt|;
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
name|AbstractXmlSecInHandler
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|WSProviderConfig
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|allowEmptyBody
decl_stmt|;
specifier|public
name|void
name|setAllowEmptyBody
parameter_list|(
name|boolean
name|allow
parameter_list|)
block|{
name|this
operator|.
name|allowEmptyBody
operator|=
name|allow
expr_stmt|;
block|}
specifier|protected
name|Document
name|getDocument
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|method
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
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Document
name|doc
init|=
literal|null
decl_stmt|;
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
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
literal|"UTF-8"
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
name|throwFault
argument_list|(
literal|"Invalid XML payload"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|XMLStreamReader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|instanceof
name|W3CDOMStreamReader
condition|)
block|{
name|doc
operator|=
operator|(
operator|(
name|W3CDOMStreamReader
operator|)
name|reader
operator|)
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|doc
operator|==
literal|null
operator|&&
operator|!
name|allowEmptyBody
condition|)
block|{
name|throwFault
argument_list|(
literal|"No payload is available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
return|;
block|}
specifier|protected
name|void
name|throwFault
parameter_list|(
name|String
name|error
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|JAXRSUtils
operator|.
name|toResponseBuilder
argument_list|(
literal|400
argument_list|)
operator|.
name|entity
argument_list|(
name|error
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
literal|null
argument_list|,
name|response
argument_list|)
throw|;
block|}
specifier|protected
name|Element
name|getNode
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|ns
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|NodeList
name|list
init|=
name|parent
operator|.
name|getElementsByTagNameNS
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
operator|&&
name|list
operator|.
name|getLength
argument_list|()
operator|>=
name|index
operator|+
literal|1
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|list
operator|.
name|item
argument_list|(
name|index
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

