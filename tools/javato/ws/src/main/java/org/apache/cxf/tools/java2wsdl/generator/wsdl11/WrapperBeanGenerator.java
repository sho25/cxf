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
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaClass
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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|RequestWrapper
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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|ResponseWrapper
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WrapperBeanGenerator
extends|extends
name|BeanGenerator
block|{
specifier|protected
name|Collection
argument_list|<
name|JavaClass
argument_list|>
name|generateBeanClasses
parameter_list|(
specifier|final
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
name|Collection
argument_list|<
name|JavaClass
argument_list|>
name|wrapperClasses
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationInfo
name|op
range|:
name|serviceInfo
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|op
operator|.
name|getUnwrappedOperation
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|op
operator|.
name|hasInput
argument_list|()
condition|)
block|{
name|RequestWrapper
name|requestWrapper
init|=
operator|new
name|RequestWrapper
argument_list|()
decl_stmt|;
name|requestWrapper
operator|.
name|setOperationInfo
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|JavaClass
name|jClass
init|=
name|requestWrapper
operator|.
name|buildWrapperBeanClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestWrapper
operator|.
name|isWrapperBeanClassNotExist
argument_list|()
condition|)
block|{
name|wrapperClasses
operator|.
name|add
argument_list|(
name|jClass
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|op
operator|.
name|hasOutput
argument_list|()
condition|)
block|{
name|ResponseWrapper
name|responseWrapper
init|=
operator|new
name|ResponseWrapper
argument_list|()
decl_stmt|;
name|responseWrapper
operator|.
name|setOperationInfo
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|JavaClass
name|jClass
init|=
name|responseWrapper
operator|.
name|buildWrapperBeanClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseWrapper
operator|.
name|isWrapperBeanClassNotExist
argument_list|()
condition|)
block|{
name|wrapperClasses
operator|.
name|add
argument_list|(
name|jClass
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|wrapperClasses
return|;
block|}
block|}
end_class

end_unit

