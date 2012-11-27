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
name|utils
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
name|Method
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
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
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|BeanParam
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
name|BindingPriority
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
name|CookieParam
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
name|DefaultValue
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
name|FormParam
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
name|HeaderParam
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
name|HttpMethod
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
name|MatrixParam
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
name|NameBinding
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
name|PathParam
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
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
name|container
operator|.
name|ResourceInfo
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
name|Application
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
name|Context
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
name|HttpHeaders
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
name|Request
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
name|SecurityContext
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
name|UriInfo
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
name|ContextResolver
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
name|Providers
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|AnnotationUtils
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
name|AnnotationUtils
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
name|AnnotationUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|CONTEXT_CLASSES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|PARAM_ANNOTATION_CLASSES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|METHOD_ANNOTATION_CLASSES
decl_stmt|;
static|static
block|{
name|CONTEXT_CLASSES
operator|=
name|initContextClasses
argument_list|()
expr_stmt|;
name|PARAM_ANNOTATION_CLASSES
operator|=
name|initParamAnnotationClasses
argument_list|()
expr_stmt|;
name|METHOD_ANNOTATION_CLASSES
operator|=
name|initMethodAnnotationClasses
argument_list|()
expr_stmt|;
block|}
specifier|private
name|AnnotationUtils
parameter_list|()
block|{      }
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|initContextClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|UriInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ContextResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Providers
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Request
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ResourceInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Application
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Servlet API
try|try
block|{
name|classes
operator|.
name|add
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ServletContext
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// it is not a problem on the client side and the exception will be
comment|// thrown later on if the injection of one of these contexts will be
comment|// attempted on the server side
name|LOG
operator|.
name|fine
argument_list|(
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
literal|"NO_SERVLET_API"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// CXF-specific
name|classes
operator|.
name|add
argument_list|(
name|MessageContext
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|initParamAnnotationClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|PathParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|QueryParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|MatrixParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|HeaderParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|CookieParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|FormParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BeanParam
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|initMethodAnnotationClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|HttpMethod
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Path
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Produces
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|Consumes
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
specifier|public
specifier|static
name|int
name|getBindingPriority
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerCls
parameter_list|)
block|{
name|BindingPriority
name|b
init|=
name|providerCls
operator|.
name|getAnnotation
argument_list|(
name|BindingPriority
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|b
operator|==
literal|null
condition|?
name|BindingPriority
operator|.
name|USER
else|:
name|b
operator|.
name|value
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getNameBindings
parameter_list|(
name|Annotation
index|[]
name|targetAnns
parameter_list|)
block|{
if|if
condition|(
name|targetAnns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|a
range|:
name|targetAnns
control|)
block|{
name|NameBinding
name|nb
init|=
name|a
operator|.
name|annotationType
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|NameBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|nb
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|names
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isContextClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|contextClass
parameter_list|)
block|{
return|return
name|CONTEXT_CLASSES
operator|.
name|contains
argument_list|(
name|contextClass
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isParamAnnotationClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|annotationClass
parameter_list|)
block|{
return|return
name|PARAM_ANNOTATION_CLASSES
operator|.
name|contains
argument_list|(
name|annotationClass
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isValidParamAnnotationClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|annotationClass
parameter_list|)
block|{
return|return
name|PARAM_ANNOTATION_CLASSES
operator|.
name|contains
argument_list|(
name|annotationClass
argument_list|)
operator|||
name|Context
operator|.
name|class
operator|==
name|annotationClass
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isValidParamAnnotations
parameter_list|(
name|Annotation
index|[]
name|paramAnnotations
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|a
range|:
name|paramAnnotations
control|)
block|{
if|if
condition|(
name|AnnotationUtils
operator|.
name|isValidParamAnnotationClass
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isMethodAnnotation
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
return|return
name|METHOD_ANNOTATION_CLASSES
operator|.
name|contains
argument_list|(
name|a
operator|.
name|annotationType
argument_list|()
argument_list|)
operator|||
name|a
operator|.
name|annotationType
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|HttpMethod
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getAnnotationValue
parameter_list|(
name|Annotation
name|a
parameter_list|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|PathParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|PathParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|QueryParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|QueryParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|MatrixParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|MatrixParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|HeaderParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|HeaderParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|CookieParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|CookieParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|FormParam
operator|.
name|class
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|FormParam
operator|)
name|a
operator|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getAnnotation
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|anns
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|Annotation
name|a
range|:
name|anns
control|)
block|{
if|if
condition|(
name|a
operator|.
name|annotationType
argument_list|()
operator|==
name|type
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|a
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Method
name|getAnnotatedMethod
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
name|Method
name|annotatedMethod
init|=
name|doGetAnnotatedMethod
argument_list|(
name|m
argument_list|)
decl_stmt|;
return|return
name|annotatedMethod
operator|==
literal|null
condition|?
name|m
else|:
name|annotatedMethod
return|;
block|}
specifier|private
specifier|static
name|Method
name|doGetAnnotatedMethod
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
return|return
name|m
return|;
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
name|isMethodAnnotation
argument_list|(
name|a
argument_list|)
condition|)
block|{
return|return
name|m
return|;
block|}
block|}
for|for
control|(
name|Annotation
index|[]
name|paramAnnotations
range|:
name|m
operator|.
name|getParameterAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|isValidParamAnnotations
argument_list|(
name|paramAnnotations
argument_list|)
condition|)
block|{
return|return
name|m
return|;
block|}
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|superC
init|=
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superC
operator|!=
literal|null
operator|&&
name|Object
operator|.
name|class
operator|!=
name|superC
condition|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|doGetAnnotatedMethod
argument_list|(
name|superC
operator|.
name|getMethod
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|doGetAnnotatedMethod
argument_list|(
name|i
operator|.
name|getMethod
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getHttpMethodValue
parameter_list|(
name|Method
name|m
parameter_list|)
block|{
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
name|HttpMethod
name|httpM
init|=
name|a
operator|.
name|annotationType
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|HttpMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpM
operator|!=
literal|null
condition|)
block|{
return|return
name|httpM
operator|.
name|value
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
name|getMethodAnnotation
parameter_list|(
name|Method
name|m
parameter_list|,
name|Class
argument_list|<
name|A
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
name|m
operator|==
literal|null
condition|?
literal|null
else|:
name|m
operator|.
name|getAnnotation
argument_list|(
name|aClass
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
name|getClassAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Class
argument_list|<
name|A
argument_list|>
name|aClass
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|A
name|p
init|=
name|c
operator|.
name|getAnnotation
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
name|p
return|;
block|}
name|p
operator|=
name|getClassAnnotation
argument_list|(
name|c
operator|.
name|getSuperclass
argument_list|()
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
name|p
return|;
block|}
comment|// finally try the first one on the interface
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|c
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|p
operator|=
name|getClassAnnotation
argument_list|(
name|i
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
name|p
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getDefaultParameterValue
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|DefaultValue
name|dv
init|=
name|AnnotationUtils
operator|.
name|getAnnotation
argument_list|(
name|anns
argument_list|,
name|DefaultValue
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|dv
operator|!=
literal|null
condition|?
name|dv
operator|.
name|value
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

