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
name|rt
operator|.
name|security
operator|.
name|xacml
operator|.
name|pdp
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * A RuntimeException that can be thrown by a PDP implementation.  */
end_comment

begin_class
specifier|public
class|class
name|PDPException
extends|extends
name|RuntimeException
block|{
comment|/**      *       */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5618129354705668073L
decl_stmt|;
specifier|public
name|PDPException
parameter_list|(
name|String
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
name|PDPException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

