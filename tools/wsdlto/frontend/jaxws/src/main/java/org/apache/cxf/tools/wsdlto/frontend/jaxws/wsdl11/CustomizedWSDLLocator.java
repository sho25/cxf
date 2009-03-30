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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|catalog
operator|.
name|OASISCatalogManager
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
name|helpers
operator|.
name|XMLUtils
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

begin_class
specifier|public
class|class
name|CustomizedWSDLLocator
implements|implements
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
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
name|String
name|baseUri
decl_stmt|;
specifier|private
name|String
name|importedUri
decl_stmt|;
specifier|private
name|OASISCatalogManager
name|catalogResolver
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|elementMap
decl_stmt|;
specifier|private
name|String
name|latestImportURI
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resolvedMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|resolveFromMap
decl_stmt|;
specifier|public
name|CustomizedWSDLLocator
parameter_list|(
name|String
name|wsdlUrl
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|wsdlUrl
operator|=
name|wsdlUrl
expr_stmt|;
name|this
operator|.
name|baseUri
operator|=
name|this
operator|.
name|wsdlUrl
expr_stmt|;
name|resolver
operator|=
operator|new
name|ExtendedURIResolver
argument_list|()
expr_stmt|;
name|elementMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|setCatalogResolver
parameter_list|(
specifier|final
name|OASISCatalogManager
name|cr
parameter_list|)
block|{
name|this
operator|.
name|catalogResolver
operator|=
name|cr
expr_stmt|;
block|}
specifier|private
name|InputSource
name|resolve
parameter_list|(
specifier|final
name|String
name|target
parameter_list|,
specifier|final
name|String
name|base
parameter_list|)
block|{
try|try
block|{
name|String
name|resolvedLocation
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|catalogResolver
operator|!=
literal|null
condition|)
block|{
name|resolvedLocation
operator|=
name|catalogResolver
operator|.
name|resolveSystem
argument_list|(
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|resolvedLocation
operator|==
literal|null
condition|)
block|{
name|resolvedLocation
operator|=
name|catalogResolver
operator|.
name|resolveURI
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resolvedLocation
operator|==
literal|null
condition|)
block|{
name|resolvedLocation
operator|=
name|catalogResolver
operator|.
name|resolvePublic
argument_list|(
name|target
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|resolvedLocation
operator|==
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|resolver
operator|.
name|resolve
argument_list|(
name|target
argument_list|,
name|base
argument_list|)
return|;
block|}
else|else
block|{
name|resolvedMap
operator|.
name|put
argument_list|(
name|target
argument_list|,
name|resolvedLocation
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|resolver
operator|.
name|resolve
argument_list|(
name|resolvedLocation
argument_list|,
name|base
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Catalog resolve failed: "
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|InputSource
name|getBaseInputSource
parameter_list|()
block|{
if|if
condition|(
name|elementMap
operator|.
name|get
argument_list|(
name|baseUri
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Element
name|ele
init|=
name|elementMap
operator|.
name|get
argument_list|(
name|baseUri
argument_list|)
decl_stmt|;
name|String
name|content
init|=
name|XMLUtils
operator|.
name|toString
argument_list|(
name|ele
argument_list|)
decl_stmt|;
name|InputSource
name|ins
init|=
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|ins
operator|.
name|setSystemId
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
return|return
name|ins
return|;
block|}
name|InputSource
name|result
init|=
name|resolve
argument_list|(
name|baseUri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
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
return|return
name|baseUri
return|;
block|}
specifier|public
name|String
name|getLatestImportURI
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|resolveFromMap
condition|)
block|{
return|return
name|this
operator|.
name|latestImportURI
return|;
block|}
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
name|baseUri
operator|=
name|parent
expr_stmt|;
name|importedUri
operator|=
name|importLocation
expr_stmt|;
try|try
block|{
name|URI
name|importURI
init|=
operator|new
name|URI
argument_list|(
name|importLocation
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|importURI
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|URI
name|parentURI
init|=
operator|new
name|URI
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|importURI
operator|=
name|parentURI
operator|.
name|resolve
argument_list|(
name|importURI
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|elementMap
operator|.
name|get
argument_list|(
name|importURI
operator|.
name|toString
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Element
name|ele
init|=
name|elementMap
operator|.
name|get
argument_list|(
name|importURI
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|content
init|=
name|XMLUtils
operator|.
name|toString
argument_list|(
name|ele
argument_list|)
decl_stmt|;
name|InputSource
name|ins
init|=
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|content
argument_list|)
argument_list|)
decl_stmt|;
name|ins
operator|.
name|setSystemId
argument_list|(
name|importURI
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|resolveFromMap
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|latestImportURI
operator|=
name|importURI
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|ins
return|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to Resolve "
operator|+
name|importLocation
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|resolveFromMap
operator|=
literal|false
expr_stmt|;
return|return
name|resolve
argument_list|(
name|importedUri
argument_list|,
name|baseUri
argument_list|)
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
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getResolvedMap
parameter_list|()
block|{
return|return
name|resolvedMap
return|;
block|}
block|}
end_class

end_unit

