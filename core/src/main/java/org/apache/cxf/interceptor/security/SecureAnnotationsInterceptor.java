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
name|interceptor
operator|.
name|security
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|common
operator|.
name|util
operator|.
name|ClassHelper
import|;
end_import

begin_class
specifier|public
class|class
name|SecureAnnotationsInterceptor
extends|extends
name|SimpleAuthorizingInterceptor
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
name|SecureAnnotationsInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_ANNOTATION_CLASS_NAME
init|=
literal|"javax.annotation.security.RolesAllowed"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SKIP_METHODS
decl_stmt|;
static|static
block|{
name|SKIP_METHODS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|SKIP_METHODS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"wait"
block|,
literal|"notify"
block|,
literal|"notifyAll"
block|,
literal|"equals"
block|,
literal|"toString"
block|,
literal|"hashCode"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|annotationClassName
init|=
name|DEFAULT_ANNOTATION_CLASS_NAME
decl_stmt|;
specifier|public
name|SecureAnnotationsInterceptor
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SecureAnnotationsInterceptor
parameter_list|(
name|boolean
name|uniqueId
parameter_list|)
block|{
name|super
argument_list|(
name|uniqueId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAnnotationClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|SecureAnnotationsInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|annotationClassName
operator|=
name|name
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Annotation class "
operator|+
name|name
operator|+
literal|" is not available"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setSecuredObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rolesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|findRoles
argument_list|(
name|cls
argument_list|,
name|rolesMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|rolesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"The roles map is empty, the service object is not protected"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|rolesMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Method: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|", roles: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|super
operator|.
name|setMethodRolesMap
argument_list|(
name|rolesMap
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|findRoles
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rolesMap
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
operator|||
name|cls
operator|==
name|Object
operator|.
name|class
condition|)
block|{
return|return;
block|}
name|String
name|classRolesAllowed
init|=
name|getRoles
argument_list|(
name|cls
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|annotationClassName
argument_list|)
decl_stmt|;
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
name|SKIP_METHODS
operator|.
name|contains
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|methodRolesAllowed
init|=
name|getRoles
argument_list|(
name|m
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|annotationClassName
argument_list|)
decl_stmt|;
name|String
name|theRoles
init|=
name|methodRolesAllowed
operator|!=
literal|null
condition|?
name|methodRolesAllowed
else|:
name|classRolesAllowed
decl_stmt|;
if|if
condition|(
name|theRoles
operator|!=
literal|null
condition|)
block|{
name|rolesMap
operator|.
name|put
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|theRoles
argument_list|)
expr_stmt|;
name|rolesMap
operator|.
name|put
argument_list|(
name|createMethodSig
argument_list|(
name|m
argument_list|)
argument_list|,
name|theRoles
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|rolesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|findRoles
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|,
name|rolesMap
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|rolesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|interfaceCls
range|:
name|cls
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|findRoles
argument_list|(
name|interfaceCls
argument_list|,
name|rolesMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getRoles
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|,
name|String
name|annName
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|anns
control|)
block|{
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|annName
argument_list|)
condition|)
block|{
try|try
block|{
name|Method
name|valueMethod
init|=
name|ann
operator|.
name|annotationType
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"value"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|String
index|[]
name|roles
init|=
operator|(
name|String
index|[]
operator|)
name|valueMethod
operator|.
name|invoke
argument_list|(
name|ann
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|roles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|roles
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|roles
operator|.
name|length
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
break|break;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

