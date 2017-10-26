begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Bus
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
name|util
operator|.
name|ReflectionUtil
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
name|AnnotationUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|BeanResourceInfo
extends|extends
name|AbstractResourceInfo
block|{
specifier|protected
name|List
argument_list|<
name|Field
argument_list|>
name|paramFields
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Method
argument_list|>
name|paramMethods
decl_stmt|;
specifier|private
name|boolean
name|paramsAvailable
decl_stmt|;
specifier|protected
name|BeanResourceInfo
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|BeanResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|(
name|resourceClass
argument_list|,
name|serviceClass
argument_list|,
name|isRoot
argument_list|,
literal|true
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|BeanResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|boolean
name|checkContexts
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|resourceClass
argument_list|,
name|serviceClass
argument_list|,
name|isRoot
argument_list|,
name|checkContexts
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkContexts
operator|&&
name|resourceClass
operator|!=
literal|null
condition|)
block|{
name|setParamField
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
name|setParamMethods
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|paramsAvailable
parameter_list|()
block|{
return|return
name|paramsAvailable
return|;
block|}
specifier|private
name|void
name|setParamField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|Object
operator|.
name|class
operator|==
name|cls
operator|||
name|cls
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Field
name|f
range|:
name|ReflectionUtil
operator|.
name|getDeclaredFields
argument_list|(
name|cls
argument_list|)
control|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|f
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|AnnotationUtils
operator|.
name|isParamAnnotationClass
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|paramFields
operator|==
literal|null
condition|)
block|{
name|paramFields
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|paramsAvailable
operator|=
literal|true
expr_stmt|;
name|paramFields
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|setParamField
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setParamMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
for|for
control|(
name|Method
name|m
range|:
name|cls
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|m
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"set"
argument_list|)
operator|||
name|m
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Annotation
name|a
range|:
name|m
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|AnnotationUtils
operator|.
name|isParamAnnotationClass
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|addParamMethod
argument_list|(
name|m
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|cls
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|interfaces
control|)
block|{
name|setParamMethods
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addParamMethod
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
if|if
condition|(
name|paramMethods
operator|==
literal|null
condition|)
block|{
name|paramMethods
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|paramsAvailable
operator|=
literal|true
expr_stmt|;
name|paramMethods
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Method
argument_list|>
name|getParameterMethods
parameter_list|()
block|{
return|return
name|paramMethods
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|Method
operator|>
name|emptyList
argument_list|()
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|paramMethods
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Field
argument_list|>
name|getParameterFields
parameter_list|()
block|{
return|return
name|paramFields
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|Field
operator|>
name|emptyList
argument_list|()
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|paramFields
argument_list|)
return|;
block|}
block|}
end_class

end_unit

