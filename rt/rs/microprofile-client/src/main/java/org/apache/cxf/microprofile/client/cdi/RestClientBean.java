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
name|microprofile
operator|.
name|client
operator|.
name|cdi
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
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Arrays
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
name|List
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
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|Dependent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Default
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|PassivationCapable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
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
name|microprofile
operator|.
name|client
operator|.
name|CxfTypeSafeClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|config
operator|.
name|ConfigProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|inject
operator|.
name|RestClient
import|;
end_import

begin_class
specifier|public
class|class
name|RestClientBean
implements|implements
name|Bean
argument_list|<
name|Object
argument_list|>
implements|,
name|PassivationCapable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REST_URL_FORMAT
init|=
literal|"%s/mp-rest/url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REST_SCOPE_FORMAT
init|=
literal|"%s/mp-rest/scope"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Default
name|DEFAULT_LITERAL
init|=
operator|new
name|DefaultLiteral
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clientInterface
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|scope
decl_stmt|;
specifier|private
specifier|final
name|BeanManager
name|beanManager
decl_stmt|;
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
name|RestClientBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clientInterface
parameter_list|,
name|BeanManager
name|beanManager
parameter_list|)
block|{
name|this
operator|.
name|clientInterface
operator|=
name|clientInterface
expr_stmt|;
name|this
operator|.
name|beanManager
operator|=
name|beanManager
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|ConfigProvider
operator|.
name|getConfig
argument_list|()
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|this
operator|.
name|readScope
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|clientInterface
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|()
block|{
return|return
name|clientInterface
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|InjectionPoint
argument_list|>
name|getInjectionPoints
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isNullable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|Object
argument_list|>
name|creationalContext
parameter_list|)
block|{
name|CxfTypeSafeClientBuilder
name|builder
init|=
operator|new
name|CxfTypeSafeClientBuilder
argument_list|()
decl_stmt|;
name|String
name|baseUrl
init|=
name|getBaseUrl
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|builder
operator|.
name|baseUrl
argument_list|(
operator|new
name|URL
argument_list|(
name|baseUrl
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|clientInterface
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The value of URL was invalid "
operator|+
name|baseUrl
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|(
name|Object
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|Object
argument_list|>
name|creationalContext
parameter_list|)
block|{      }
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|clientInterface
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|DEFAULT_LITERAL
argument_list|,
name|RestClient
operator|.
name|RestClientLiteral
operator|.
name|LITERAL
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|clientInterface
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|getStereotypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAlternative
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|getBaseUrl
parameter_list|()
block|{
name|String
name|property
init|=
name|String
operator|.
name|format
argument_list|(
name|REST_URL_FORMAT
argument_list|,
name|clientInterface
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|config
operator|.
name|getValue
argument_list|(
name|property
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|readScope
parameter_list|()
block|{
comment|// first check to see if the value is set
name|String
name|property
init|=
name|String
operator|.
name|format
argument_list|(
name|REST_SCOPE_FORMAT
argument_list|,
name|clientInterface
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|configuredScope
init|=
name|config
operator|.
name|getOptionalValue
argument_list|(
name|property
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuredScope
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|configuredScope
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The scope "
operator|+
name|configuredScope
operator|+
literal|" is invalid"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|Annotation
argument_list|>
name|possibleScopes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Annotation
index|[]
name|annotations
init|=
name|clientInterface
operator|.
name|getDeclaredAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|beanManager
operator|.
name|isScope
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|possibleScopes
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|possibleScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Dependent
operator|.
name|class
return|;
block|}
elseif|else
if|if
condition|(
name|possibleScopes
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|possibleScopes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|annotationType
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The client interface "
operator|+
name|clientInterface
operator|+
literal|" has multiple scopes defined "
operator|+
name|possibleScopes
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|DefaultLiteral
extends|extends
name|AnnotationLiteral
argument_list|<
name|Default
argument_list|>
implements|implements
name|Default
block|{      }
block|}
end_class

end_unit

