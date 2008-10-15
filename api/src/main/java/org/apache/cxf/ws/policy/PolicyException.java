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
name|ws
operator|.
name|policy
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

begin_comment
comment|/**  * PolicyException is the type of RuntimeException thrown by any exceptions encountered  * by the policy framework.  */
end_comment

begin_class
specifier|public
class|class
name|PolicyException
extends|extends
name|UncheckedException
block|{
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
name|PolicyException
operator|.
name|class
argument_list|,
literal|"APIMessages"
argument_list|)
decl_stmt|;
specifier|public
name|PolicyException
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyException
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
block|}
specifier|public
name|PolicyException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PolicyException
parameter_list|(
name|AssertionInfo
name|info
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ASSERTION_NOT_ASSERTED"
argument_list|,
name|BUNDLE
argument_list|,
name|info
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|info
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

