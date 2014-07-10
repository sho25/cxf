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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
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
name|i18n
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
name|common
operator|.
name|i18n
operator|.
name|UncheckedException
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
name|security
operator|.
name|AccessDeniedException
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
name|security
operator|.
name|AuthenticationException
import|;
end_import

begin_comment
comment|/**  * A Fault that occurs during invocation processing.  */
end_comment

begin_class
specifier|public
class|class
name|Fault
extends|extends
name|UncheckedException
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|FAULT_CODE_CLIENT
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/faultcode"
argument_list|,
literal|"client"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|FAULT_CODE_SERVER
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/faultcode"
argument_list|,
literal|"server"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STACKTRACE_NAMESPACE
init|=
literal|"http://cxf.apache.org/fault"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STACKTRACE
init|=
literal|"stackTrace"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_HTTP_RESPONSE_CODE
init|=
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1583932965031558864L
decl_stmt|;
specifier|private
name|Element
name|detail
decl_stmt|;
specifier|private
name|String
name|messageString
decl_stmt|;
specifier|private
name|QName
name|code
decl_stmt|;
specifier|private
name|String
name|lang
decl_stmt|;
comment|/**      * response http header status code      */
specifier|private
name|int
name|statusCode
init|=
name|DEFAULT_HTTP_RESPONSE_CODE
decl_stmt|;
specifier|public
name|Fault
parameter_list|(
name|Message
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
name|code
operator|=
name|FAULT_CODE_SERVER
expr_stmt|;
name|determineStatusCode
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
name|code
operator|=
name|FAULT_CODE_SERVER
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|log
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|b
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|Logger
name|log
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|log
argument_list|)
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|b
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|b
argument_list|)
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|Logger
name|log
parameter_list|,
name|Throwable
name|t
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|log
argument_list|,
name|params
argument_list|)
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|String
name|message
parameter_list|,
name|ResourceBundle
name|b
parameter_list|,
name|Throwable
name|t
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Message
argument_list|(
name|message
argument_list|,
name|b
argument_list|,
name|params
argument_list|)
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|super
argument_list|(
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|messageString
operator|=
name|t
operator|==
literal|null
condition|?
literal|null
else|:
name|t
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|code
operator|=
name|FAULT_CODE_SERVER
expr_stmt|;
name|determineStatusCode
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|Message
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|,
name|QName
name|fc
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
name|code
operator|=
name|fc
expr_stmt|;
name|determineStatusCode
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|Message
name|message
parameter_list|,
name|QName
name|fc
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
name|code
operator|=
name|fc
expr_stmt|;
block|}
specifier|public
name|Fault
parameter_list|(
name|Throwable
name|t
parameter_list|,
name|QName
name|fc
parameter_list|)
block|{
name|super
argument_list|(
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|messageString
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|messageString
operator|=
name|t
operator|==
literal|null
condition|?
literal|null
else|:
name|t
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
name|code
operator|=
name|fc
expr_stmt|;
name|determineStatusCode
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|determineStatusCode
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|throwable
operator|instanceof
name|AuthenticationException
operator|||
name|throwable
operator|instanceof
name|LoginException
condition|)
block|{
name|statusCode
operator|=
name|HttpURLConnection
operator|.
name|HTTP_UNAUTHORIZED
expr_stmt|;
block|}
if|if
condition|(
name|throwable
operator|instanceof
name|AccessDeniedException
condition|)
block|{
name|statusCode
operator|=
name|HttpURLConnection
operator|.
name|HTTP_FORBIDDEN
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|messageString
return|;
block|}
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|messageString
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|QName
name|getFaultCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
specifier|public
name|Fault
name|setFaultCode
parameter_list|(
name|QName
name|c
parameter_list|)
block|{
name|code
operator|=
name|c
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns the detail node.      * @return the detail node.      */
specifier|public
name|Element
name|getDetail
parameter_list|()
block|{
return|return
name|detail
return|;
block|}
comment|/**      * Sets a details<code>Node</code> on this fault.      *       * @param details the detail node.      */
specifier|public
name|void
name|setDetail
parameter_list|(
name|Element
name|details
parameter_list|)
block|{
name|detail
operator|=
name|details
expr_stmt|;
block|}
comment|/**      * Indicates whether this fault has a detail message.      *       * @return<code>true</code> if this fault has a detail message;      *<code>false</code> otherwise.      */
specifier|public
name|boolean
name|hasDetails
parameter_list|()
block|{
return|return
name|this
operator|.
name|detail
operator|!=
literal|null
return|;
block|}
comment|/**      * Returns the detail node. If no detail node has been set, an empty      *<code>&lt;detail&gt;</code> is created.      *       * @return the detail node.      */
specifier|public
name|Element
name|getOrCreateDetail
parameter_list|()
block|{
if|if
condition|(
name|detail
operator|==
literal|null
condition|)
block|{
name|detail
operator|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"detail"
argument_list|)
expr_stmt|;
block|}
return|return
name|detail
return|;
block|}
comment|/**      * Returns  http header status code.      * @return status code.      */
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|this
operator|.
name|statusCode
return|;
block|}
comment|/**      * Set http header status code on this fault.      *       * @param statusCode      */
specifier|public
name|void
name|setStatusCode
parameter_list|(
name|int
name|statusCode
parameter_list|)
block|{
name|this
operator|.
name|statusCode
operator|=
name|statusCode
expr_stmt|;
block|}
specifier|public
name|void
name|setLang
parameter_list|(
name|String
name|convertedLang
parameter_list|)
block|{
name|lang
operator|=
name|convertedLang
expr_stmt|;
block|}
specifier|public
name|String
name|getLang
parameter_list|()
block|{
return|return
name|lang
return|;
block|}
block|}
end_class

end_unit

