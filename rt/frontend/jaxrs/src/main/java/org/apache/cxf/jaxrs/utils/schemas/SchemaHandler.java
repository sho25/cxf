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
operator|.
name|schemas
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
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
name|ls
operator|.
name|LSInput
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
name|ls
operator|.
name|LSResourceResolver
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
name|BusFactory
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
name|common
operator|.
name|util
operator|.
name|ClasspathScanner
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
name|xmlschema
operator|.
name|LSInputImpl
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
name|ResourceUtils
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
name|constants
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
class|class
name|SchemaHandler
block|{
specifier|static
specifier|final
name|String
name|DEFAULT_CATALOG_LOCATION
init|=
literal|"classpath:META-INF/jax-rs-catalog.xml"
decl_stmt|;
specifier|private
name|Schema
name|schema
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|String
name|catalogLocation
decl_stmt|;
specifier|public
name|SchemaHandler
parameter_list|()
block|{      }
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|setSchemas
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
block|{
name|setSchemaLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
block|{
name|schema
operator|=
name|createSchema
argument_list|(
name|locations
argument_list|,
name|catalogLocation
argument_list|,
name|bus
operator|==
literal|null
condition|?
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
else|:
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCatalogLocation
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|catalogLocation
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Schema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
specifier|static
name|Schema
name|createSchema
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|,
name|String
name|catalogLocation
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|SchemaFactory
name|factory
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|)
decl_stmt|;
name|Schema
name|s
init|=
literal|null
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|Source
argument_list|>
name|sources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|loc
range|:
name|locations
control|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|schemaURLs
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|loc
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|==
operator|-
literal|1
operator|||
name|loc
operator|.
name|lastIndexOf
argument_list|(
literal|'*'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|schemaURLs
operator|=
name|ClasspathScanner
operator|.
name|findResources
argument_list|(
name|loc
argument_list|,
literal|"xsd"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URL
name|url
init|=
name|ResourceUtils
operator|.
name|getResourceURL
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|schemaURLs
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|schemaURLs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find XML schema location: "
operator|+
name|loc
argument_list|)
throw|;
block|}
for|for
control|(
name|URL
name|schemaURL
range|:
name|schemaURLs
control|)
block|{
name|Reader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|schemaURL
operator|.
name|openStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|source
operator|.
name|setSystemId
argument_list|(
name|schemaURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sources
operator|.
name|add
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sources
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
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
if|if
condition|(
name|catalogResolver
operator|!=
literal|null
condition|)
block|{
name|catalogLocation
operator|=
name|catalogLocation
operator|==
literal|null
condition|?
name|SchemaHandler
operator|.
name|DEFAULT_CATALOG_LOCATION
else|:
name|catalogLocation
expr_stmt|;
name|URL
name|catalogURL
init|=
name|ResourceUtils
operator|.
name|getResourceURL
argument_list|(
name|catalogLocation
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|catalogURL
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|catalogResolver
operator|.
name|loadCatalog
argument_list|(
name|catalogURL
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setResourceResolver
argument_list|(
operator|new
name|LSResourceResolver
argument_list|()
block|{
specifier|public
name|LSInput
name|resolveResource
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|baseURI
parameter_list|)
block|{
try|try
block|{
name|String
name|resolvedLocation
init|=
name|catalogResolver
operator|.
name|resolveSystem
argument_list|(
name|systemId
argument_list|)
decl_stmt|;
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
name|namespaceURI
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
name|publicId
operator|!=
literal|null
condition|?
name|publicId
else|:
name|namespaceURI
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resolvedLocation
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|resourceStream
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|resolvedLocation
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceStream
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|LSInputImpl
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|,
name|resourceStream
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Catalog "
operator|+
name|catalogLocation
operator|+
literal|" can not be loaded"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
name|s
operator|=
name|factory
operator|.
name|newSchema
argument_list|(
name|sources
operator|.
name|toArray
argument_list|(
operator|new
name|Source
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to load XML schema : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
return|return
name|s
return|;
block|}
block|}
end_class

end_unit

