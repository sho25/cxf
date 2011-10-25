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
name|jaxrs
operator|.
name|impl
package|;
end_package

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
name|Level
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
name|WebApplicationException
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
name|Response
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
name|ext
operator|.
name|ExceptionMapper
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
name|BundleUtils
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
name|logging
operator|.
name|FaultListener
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
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
class|class
name|WebApplicationExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|WebApplicationException
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
name|WebApplicationExceptionMapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|WebApplicationExceptionMapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|printStackTrace
decl_stmt|;
specifier|public
name|Response
name|toResponse
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|String
name|message
init|=
name|ex
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|?
name|ex
operator|.
name|getMessage
argument_list|()
else|:
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
literal|"cause is "
operator|+
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
literal|"no cause is available"
expr_stmt|;
block|}
block|}
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
name|errorMsg
init|=
operator|new
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
argument_list|(
literal|"WEB_APP_EXCEPTION"
argument_list|,
name|BUNDLE
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|Response
operator|.
name|status
argument_list|(
literal|500
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|entity
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|Message
name|msg
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|FaultListener
name|flogger
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|flogger
operator|=
operator|(
name|FaultListener
operator|)
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|doDefault
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|flogger
operator|!=
literal|null
condition|)
block|{
name|doDefault
operator|=
name|flogger
operator|.
name|faultOccurred
argument_list|(
name|ex
argument_list|,
name|message
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
operator|&&
name|doDefault
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|message
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|printStackTrace
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|public
name|void
name|setPrintStackTrace
parameter_list|(
name|boolean
name|printStackTrace
parameter_list|)
block|{
name|this
operator|.
name|printStackTrace
operator|=
name|printStackTrace
expr_stmt|;
block|}
block|}
end_class

end_unit

