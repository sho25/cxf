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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|Path
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
name|Produces
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
name|lifecycle
operator|.
name|ResourceProvider
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
class|class
name|ClassResourceInfo
extends|extends
name|AbstractResourceInfo
block|{
specifier|private
name|URITemplate
name|uriTemplate
decl_stmt|;
specifier|private
name|MethodDispatcher
name|methodDispatcher
decl_stmt|;
specifier|private
name|ResourceProvider
name|resourceProvider
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|subClassResourceInfo
init|=
operator|new
name|ArrayList
argument_list|<
name|ClassResourceInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Field
argument_list|>
name|paramFields
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Method
argument_list|>
name|paramMethods
decl_stmt|;
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theResourceClass
argument_list|,
name|theRoot
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|)
block|{
name|super
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
name|theRoot
argument_list|)
expr_stmt|;
if|if
condition|(
name|theRoot
condition|)
block|{
name|initParamFields
argument_list|()
expr_stmt|;
name|initParamMethods
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initParamFields
parameter_list|()
block|{
if|if
condition|(
name|getResourceClass
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|isRoot
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Field
name|f
range|:
name|getServiceClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
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
argument_list|<
name|Field
argument_list|>
argument_list|()
expr_stmt|;
block|}
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
block|}
specifier|private
name|void
name|initParamMethods
parameter_list|()
block|{
for|for
control|(
name|Method
name|m
range|:
name|getServiceClass
argument_list|()
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
name|checkParamMethod
argument_list|(
name|m
argument_list|,
name|AnnotationUtils
operator|.
name|getAnnotationValue
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|public
name|URITemplate
name|getURITemplate
parameter_list|()
block|{
return|return
name|uriTemplate
return|;
block|}
specifier|public
name|void
name|setURITemplate
parameter_list|(
name|URITemplate
name|u
parameter_list|)
block|{
name|uriTemplate
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|MethodDispatcher
name|getMethodDispatcher
parameter_list|()
block|{
return|return
name|methodDispatcher
return|;
block|}
specifier|public
name|void
name|setMethodDispatcher
parameter_list|(
name|MethodDispatcher
name|md
parameter_list|)
block|{
name|methodDispatcher
operator|=
name|md
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasSubResources
parameter_list|()
block|{
return|return
operator|!
name|subClassResourceInfo
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|addSubClassResourceInfo
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|subClassResourceInfo
operator|.
name|add
argument_list|(
name|cri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|getSubClassResourceInfo
parameter_list|()
block|{
return|return
name|subClassResourceInfo
return|;
block|}
specifier|public
name|ResourceProvider
name|getResourceProvider
parameter_list|()
block|{
return|return
name|resourceProvider
return|;
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|resourceProvider
operator|=
name|rp
expr_stmt|;
block|}
specifier|public
name|Produces
name|getProduceMime
parameter_list|()
block|{
return|return
operator|(
name|Produces
operator|)
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Produces
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Consumes
name|getConsumeMime
parameter_list|()
block|{
return|return
operator|(
name|Consumes
operator|)
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Consumes
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Path
name|getPath
parameter_list|()
block|{
return|return
operator|(
name|Path
operator|)
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Path
operator|.
name|class
argument_list|)
return|;
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
argument_list|<
name|Method
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|paramMethods
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|EMPTY_LIST
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|paramMethods
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|EMPTY_LIST
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|paramFields
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkParamMethod
parameter_list|(
name|Method
name|m
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"set"
operator|+
name|value
argument_list|)
condition|)
block|{
name|addParamMethod
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|resourceProvider
operator|instanceof
name|SingletonResourceProvider
return|;
block|}
block|}
end_class

end_unit

