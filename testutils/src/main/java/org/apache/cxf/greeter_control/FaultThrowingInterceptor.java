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
name|greeter_control
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
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
name|MessageUtils
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|FaultThrowingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|MessageFormat
name|MESSAGE_FORMAT
init|=
operator|new
name|MessageFormat
argument_list|(
literal|"Interceptor fault in phase {0}."
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|FaultThrowingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|FaultThrowingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|USER_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FaultThrowingInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|msg
init|=
name|MESSAGE_FORMAT
operator|.
name|format
argument_list|(
operator|new
name|Object
index|[]
block|{
name|getPhase
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|RuntimeException
argument_list|(
name|msg
argument_list|)
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

