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
name|transfer
operator|.
name|resourcefactory
package|;
end_package

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
name|HashMap
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
name|Map
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|ws
operator|.
name|addressing
operator|.
name|ReferenceParametersType
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
name|ws
operator|.
name|transfer
operator|.
name|Create
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
name|ws
operator|.
name|transfer
operator|.
name|CreateResponse
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
name|ws
operator|.
name|transfer
operator|.
name|Representation
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|Dialect
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
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|resolver
operator|.
name|ResourceReference
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
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|resolver
operator|.
name|ResourceResolver
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
name|ws
operator|.
name|transfer
operator|.
name|shared
operator|.
name|TransferConstants
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
name|ws
operator|.
name|transfer
operator|.
name|shared
operator|.
name|faults
operator|.
name|UnknownDialect
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
name|ws
operator|.
name|transfer
operator|.
name|validationtransformation
operator|.
name|ResourceTypeIdentifier
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
name|ws
operator|.
name|transfer
operator|.
name|validationtransformation
operator|.
name|ValidAndTransformHelper
import|;
end_import

begin_comment
comment|/**  * ResourceFactory implementation.  */
end_comment

begin_class
specifier|public
class|class
name|ResourceFactoryImpl
implements|implements
name|ResourceFactory
block|{
specifier|protected
name|ResourceResolver
name|resourceResolver
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ResourceTypeIdentifier
argument_list|>
name|resourceTypeIdentifiers
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Dialect
argument_list|>
name|dialects
decl_stmt|;
specifier|public
name|ResourceFactoryImpl
parameter_list|()
block|{
name|dialects
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Dialect
argument_list|>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|CreateResponse
name|create
parameter_list|(
name|Create
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|.
name|getDialect
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|body
operator|.
name|getDialect
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|dialects
operator|.
name|containsKey
argument_list|(
name|body
operator|.
name|getDialect
argument_list|()
argument_list|)
condition|)
block|{
name|Dialect
name|dialect
init|=
name|dialects
operator|.
name|get
argument_list|(
name|body
operator|.
name|getDialect
argument_list|()
argument_list|)
decl_stmt|;
name|Representation
name|representation
init|=
name|dialect
operator|.
name|processCreate
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|body
operator|.
name|setRepresentation
argument_list|(
name|representation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnknownDialect
argument_list|()
throw|;
block|}
block|}
name|ValidAndTransformHelper
operator|.
name|validationAndTransformation
argument_list|(
name|resourceTypeIdentifiers
argument_list|,
name|body
operator|.
name|getRepresentation
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ResourceReference
name|resourceReference
init|=
name|resourceResolver
operator|.
name|resolve
argument_list|(
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceReference
operator|.
name|getResourceManager
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|createLocally
argument_list|(
name|body
argument_list|,
name|resourceReference
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createRemotely
argument_list|(
name|body
argument_list|,
name|resourceReference
argument_list|)
return|;
block|}
block|}
specifier|public
name|ResourceResolver
name|getResourceResolver
parameter_list|()
block|{
return|return
name|resourceResolver
return|;
block|}
specifier|public
name|void
name|setResourceResolver
parameter_list|(
name|ResourceResolver
name|resourceResolver
parameter_list|)
block|{
name|this
operator|.
name|resourceResolver
operator|=
name|resourceResolver
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ResourceTypeIdentifier
argument_list|>
name|getResourceTypeIdentifiers
parameter_list|()
block|{
if|if
condition|(
name|resourceTypeIdentifiers
operator|==
literal|null
condition|)
block|{
name|resourceTypeIdentifiers
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|resourceTypeIdentifiers
return|;
block|}
specifier|public
name|void
name|setResourceTypeIdentifiers
parameter_list|(
name|List
argument_list|<
name|ResourceTypeIdentifier
argument_list|>
name|resourceTypeIdentifiers
parameter_list|)
block|{
name|this
operator|.
name|resourceTypeIdentifiers
operator|=
name|resourceTypeIdentifiers
expr_stmt|;
block|}
comment|/**      * Register Dialect object for URI.      * @param uri      * @param dialect       */
specifier|public
name|void
name|registerDialect
parameter_list|(
name|String
name|uri
parameter_list|,
name|Dialect
name|dialect
parameter_list|)
block|{
if|if
condition|(
name|dialects
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"URI \"%s\" is already registered"
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|dialects
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|dialect
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unregister dialect URI.      * @param uri       */
specifier|public
name|void
name|unregisterDialect
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
operator|!
name|dialects
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"URI \"%s\" is not registered"
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|dialects
operator|.
name|remove
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CreateResponse
name|createLocally
parameter_list|(
name|Create
name|body
parameter_list|,
name|ResourceReference
name|ref
parameter_list|)
block|{
name|Representation
name|representation
init|=
name|body
operator|.
name|getRepresentation
argument_list|()
decl_stmt|;
name|ReferenceParametersType
name|refParams
init|=
name|ref
operator|.
name|getResourceManager
argument_list|()
operator|.
name|create
argument_list|(
name|representation
argument_list|)
decl_stmt|;
name|CreateResponse
name|response
init|=
operator|new
name|CreateResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setResourceCreated
argument_list|(
operator|new
name|EndpointReferenceType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|ref
operator|.
name|getResourceURL
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|setReferenceParameters
argument_list|(
name|refParams
argument_list|)
expr_stmt|;
name|response
operator|.
name|setRepresentation
argument_list|(
name|body
operator|.
name|getRepresentation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
specifier|private
name|CreateResponse
name|createRemotely
parameter_list|(
name|Create
name|body
parameter_list|,
name|ResourceReference
name|ref
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|ResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|ref
operator|.
name|getResourceURL
argument_list|()
operator|+
name|TransferConstants
operator|.
name|RESOURCE_REMOTE_SUFFIX
argument_list|)
expr_stmt|;
name|ResourceFactory
name|client
init|=
operator|(
name|ResourceFactory
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|CreateResponse
name|response
init|=
name|client
operator|.
name|create
argument_list|(
name|body
argument_list|)
decl_stmt|;
comment|// Adding of endpoint address to the response.
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|setAddress
argument_list|(
operator|new
name|AttributedURIType
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|getResourceCreated
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|setValue
argument_list|(
name|ref
operator|.
name|getResourceURL
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

