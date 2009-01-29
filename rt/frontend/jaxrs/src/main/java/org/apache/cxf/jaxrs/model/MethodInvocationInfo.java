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
name|model
package|;
end_package

begin_comment
comment|/**  * A pair of {@link OperationResourceInfo} representing a resource method being invoked   * and actual {@link Class} of the object this method is invoked upon.  */
end_comment

begin_class
specifier|public
class|class
name|MethodInvocationInfo
block|{
specifier|private
name|OperationResourceInfo
name|ori
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
decl_stmt|;
specifier|public
name|MethodInvocationInfo
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|realClass
parameter_list|)
block|{
name|this
operator|.
name|ori
operator|=
name|ori
expr_stmt|;
name|this
operator|.
name|realClass
operator|=
name|realClass
expr_stmt|;
block|}
specifier|public
name|OperationResourceInfo
name|getMethodInfo
parameter_list|()
block|{
return|return
name|ori
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRealClass
parameter_list|()
block|{
return|return
name|realClass
return|;
block|}
block|}
end_class

end_unit

