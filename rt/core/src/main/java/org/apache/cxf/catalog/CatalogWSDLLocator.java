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
name|catalog
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|resource
operator|.
name|ExtendedURIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|resolver
operator|.
name|Catalog
import|;
end_import

begin_comment
comment|/**  * Resolves WSDL URIs using Apache Commons Resolver API.  */
end_comment

begin_class
specifier|public
class|class
name|CatalogWSDLLocator
implements|implements
name|WSDLLocator
block|{
specifier|private
name|String
name|wsdlUrl
decl_stmt|;
specifier|private
name|ExtendedURIResolver
name|resolver
decl_stmt|;
specifier|private
name|Catalog
name|catalogResolver
decl_stmt|;
specifier|private
name|String
name|baseUri
decl_stmt|;
specifier|public
name|CatalogWSDLLocator
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|OASISCatalogManager
name|catalogManager
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|wsdlUrl
expr_stmt|;
name|this
operator|.
name|catalogResolver
operator|=
name|catalogManager
operator|.
name|getCatalog
argument_list|()
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
operator|new
name|ExtendedURIResolver
argument_list|()
expr_stmt|;
block|}
specifier|public
name|InputSource
name|getBaseInputSource
parameter_list|()
block|{
name|InputSource
name|result
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|baseUri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|String
name|s
init|=
name|catalogResolver
operator|.
name|resolveSystem
argument_list|(
name|baseUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|resolver
operator|.
name|resolve
argument_list|(
name|s
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
name|wsdlUrl
operator|==
literal|null
operator|&&
name|result
operator|!=
literal|null
condition|)
block|{
name|wsdlUrl
operator|=
name|result
operator|.
name|getSystemId
argument_list|()
expr_stmt|;
block|}
name|baseUri
operator|=
name|resolver
operator|.
name|getURI
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
if|if
condition|(
name|wsdlUrl
operator|==
literal|null
condition|)
block|{
name|InputSource
name|is
init|=
name|getBaseInputSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|is
operator|.
name|getByteStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|getByteStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
return|return
name|wsdlUrl
return|;
block|}
specifier|public
name|String
name|getLatestImportURI
parameter_list|()
block|{
return|return
name|resolver
operator|.
name|getLatestImportURI
argument_list|()
return|;
block|}
specifier|public
name|InputSource
name|getImportInputSource
parameter_list|(
name|String
name|parent
parameter_list|,
name|String
name|importLocation
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|parent
expr_stmt|;
name|String
name|resolvedImportLocation
init|=
literal|null
decl_stmt|;
try|try
block|{
name|resolvedImportLocation
operator|=
name|this
operator|.
name|catalogResolver
operator|.
name|resolveSystem
argument_list|(
name|importLocation
argument_list|)
expr_stmt|;
if|if
condition|(
name|resolvedImportLocation
operator|==
literal|null
condition|)
block|{
name|resolvedImportLocation
operator|=
name|catalogResolver
operator|.
name|resolveURI
argument_list|(
name|importLocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resolvedImportLocation
operator|==
literal|null
condition|)
block|{
name|resolvedImportLocation
operator|=
name|catalogResolver
operator|.
name|resolvePublic
argument_list|(
name|importLocation
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Catalog resolution failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|InputSource
name|in
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resolvedImportLocation
operator|==
literal|null
condition|)
block|{
name|in
operator|=
name|this
operator|.
name|resolver
operator|.
name|resolve
argument_list|(
name|importLocation
argument_list|,
name|this
operator|.
name|baseUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|=
name|this
operator|.
name|resolver
operator|.
name|resolve
argument_list|(
name|resolvedImportLocation
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// XXX: If we return null (as per javadoc), a NPE is raised in WSDL4J code.
comment|// So let's return new InputSource() and let WSDL4J fail. Optionally,
comment|// we can throw a similar exception as in CatalogXmlSchemaURIResolver.
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|InputSource
argument_list|()
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|resolver
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

