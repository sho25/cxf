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
name|sts
operator|.
name|event
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
import|;
end_import

begin_class
specifier|public
class|class
name|STSIssueFailureEvent
extends|extends
name|AbstractSTSFailureEvent
implements|implements
name|TokenProviderParametersSupport
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OPERATION
init|=
literal|"Issue"
decl_stmt|;
specifier|public
name|STSIssueFailureEvent
parameter_list|(
name|TokenProviderParameters
name|parameters
parameter_list|,
name|long
name|duration
parameter_list|,
name|Exception
name|exception
parameter_list|)
block|{
name|super
argument_list|(
name|parameters
argument_list|,
name|duration
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|OPERATION
return|;
block|}
annotation|@
name|Override
specifier|public
name|TokenProviderParameters
name|getTokenParameters
parameter_list|()
block|{
return|return
operator|(
name|TokenProviderParameters
operator|)
name|this
operator|.
name|getSource
argument_list|()
return|;
block|}
block|}
end_class

end_unit

