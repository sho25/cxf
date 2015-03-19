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
name|saml
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
name|Form
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
name|helpers
operator|.
name|DOMUtils
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
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
name|util
operator|.
name|DOM2Writer
import|;
end_import

begin_class
specifier|public
class|class
name|SamlFormOutInterceptor
extends|extends
name|AbstractSamlOutInterceptor
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
name|SamlFormOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SAML_ELEMENT
init|=
literal|"SAMLToken"
decl_stmt|;
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
name|Form
name|form
init|=
name|getRequestForm
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|form
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Element
name|samlToken
init|=
operator|(
name|Element
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SAMLConstants
operator|.
name|SAML_TOKEN_ELEMENT
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|assertionWrapper
decl_stmt|;
if|if
condition|(
name|samlToken
operator|!=
literal|null
condition|)
block|{
name|assertionWrapper
operator|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertionWrapper
operator|=
name|createAssertion
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|assertionElement
init|=
name|assertionWrapper
operator|.
name|toDOM
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|String
name|encodedToken
init|=
name|encodeToken
argument_list|(
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|assertionElement
argument_list|)
argument_list|)
decl_stmt|;
name|updateForm
argument_list|(
name|form
argument_list|,
name|encodedToken
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
name|void
name|updateForm
parameter_list|(
name|Form
name|form
parameter_list|,
name|String
name|encodedToken
parameter_list|)
block|{
name|form
operator|.
name|param
argument_list|(
name|SAML_ELEMENT
argument_list|,
name|encodedToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|Form
name|getRequestForm
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Object
name|ct
init|=
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|==
literal|null
operator|||
operator|!
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
operator|.
name|equalsIgnoreCase
argument_list|(
name|ct
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
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
operator|!=
literal|null
operator|&&
name|objs
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Object
name|obj
init|=
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Form
condition|)
block|{
return|return
operator|(
name|Form
operator|)
name|obj
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|MultivaluedMap
condition|)
block|{
return|return
operator|new
name|Form
argument_list|(
operator|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|obj
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

