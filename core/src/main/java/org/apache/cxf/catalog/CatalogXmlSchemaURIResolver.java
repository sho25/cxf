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
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
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
name|helpers
operator|.
name|IOUtils
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
name|cxf
operator|.
name|transport
operator|.
name|TransportURIResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|resolver
operator|.
name|URIResolver
import|;
end_import

begin_comment
comment|/**  * Resolves URIs using Apache Commons Resolver API.  */
end_comment

begin_class
specifier|public
class|class
name|CatalogXmlSchemaURIResolver
implements|implements
name|URIResolver
block|{
specifier|private
name|ExtendedURIResolver
name|resolver
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|resolved
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CatalogXmlSchemaURIResolver
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
operator|new
name|TransportURIResolver
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
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
name|resolved
return|;
block|}
specifier|public
name|InputSource
name|resolveEntity
parameter_list|(
name|String
name|targetNamespace
parameter_list|,
name|String
name|schemaLocation
parameter_list|,
name|String
name|baseUri
parameter_list|)
block|{
name|String
name|resolvedSchemaLocation
init|=
literal|null
decl_stmt|;
name|OASISCatalogManager
name|catalogResolver
init|=
name|OASISCatalogManager
operator|.
name|getCatalogManager
argument_list|(
name|bus
argument_list|)
decl_stmt|;
try|try
block|{
name|resolvedSchemaLocation
operator|=
operator|new
name|OASISCatalogManagerHelper
argument_list|()
operator|.
name|resolve
argument_list|(
name|catalogResolver
argument_list|,
name|schemaLocation
argument_list|,
name|baseUri
argument_list|)
expr_stmt|;
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
literal|"Catalog resolution failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|InputSource
name|in
decl_stmt|;
if|if
condition|(
name|resolvedSchemaLocation
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
name|schemaLocation
argument_list|,
name|baseUri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resolved
operator|.
name|put
argument_list|(
name|schemaLocation
argument_list|,
name|resolvedSchemaLocation
argument_list|)
expr_stmt|;
name|in
operator|=
name|this
operator|.
name|resolver
operator|.
name|resolve
argument_list|(
name|resolvedSchemaLocation
argument_list|,
name|baseUri
argument_list|)
expr_stmt|;
block|}
comment|// If we return null, a NPE is raised in SchemaBuilder.
comment|// If we return new InputSource(), a XmlSchemaException is raised
comment|// but without any nice error message. So let's just throw a nice error here.
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|XmlSchemaException
argument_list|(
literal|"Unable to locate imported document "
operator|+
literal|"at '"
operator|+
name|schemaLocation
operator|+
literal|"'"
operator|+
operator|(
name|baseUri
operator|==
literal|null
condition|?
literal|"."
else|:
literal|", relative to '"
operator|+
name|baseUri
operator|+
literal|"'."
operator|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|in
operator|.
name|getByteStream
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|in
operator|.
name|getByteStream
argument_list|()
operator|instanceof
name|ByteArrayInputStream
operator|)
condition|)
block|{
comment|//workaround bug in XmlSchema - XmlSchema is not closing the InputStreams
comment|//that are returned for imports.  Thus, with a lot of services starting up
comment|//or a lot of schemas imported or similar, it's easy to run out of
comment|//file handles.  We'll just load the file into a byte[] and return that.
try|try
block|{
name|InputStream
name|ins
init|=
name|IOUtils
operator|.
name|loadIntoBAIS
argument_list|(
name|in
operator|.
name|getByteStream
argument_list|()
argument_list|)
decl_stmt|;
name|in
operator|.
name|setByteStream
argument_list|(
name|ins
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XmlSchemaException
argument_list|(
literal|"Unable to load imported document "
operator|+
literal|"at '"
operator|+
name|schemaLocation
operator|+
literal|"'"
operator|+
operator|(
name|baseUri
operator|==
literal|null
condition|?
literal|"."
else|:
literal|", relative to '"
operator|+
name|baseUri
operator|+
literal|"'."
operator|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

