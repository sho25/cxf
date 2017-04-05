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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|URISyntaxException
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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|EntityResolver
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|SystemPropertyAction
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
name|URIResolver
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
name|CatalogManager
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
name|tools
operator|.
name|CatalogResolver
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|OASISCatalogManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CATALOG_NAME
init|=
literal|"META-INF/jax-ws-catalog.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CATALOG_DEBUG_KEY
init|=
literal|"OASISCatalogManager.catalog.debug.level"
decl_stmt|;
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
name|OASISCatalogManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEBUG_LEVEL
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|CATALOG_DEBUG_KEY
argument_list|)
decl_stmt|;
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
name|Object
name|catalog
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|loadedCatalogs
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|OASISCatalogManager
parameter_list|()
block|{
name|resolver
operator|=
name|getResolver
argument_list|()
expr_stmt|;
name|catalog
operator|=
name|getCatalog
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OASISCatalogManager
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|resolver
operator|=
name|getResolver
argument_list|()
expr_stmt|;
name|catalog
operator|=
name|getCatalog
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|loadContextCatalogs
argument_list|(
name|DEFAULT_CATALOG_NAME
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasCatalogs
parameter_list|()
block|{
return|return
operator|!
name|loadedCatalogs
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Object
name|getCatalog
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
operator|(
name|CatalogResolver
operator|)
name|resolver
operator|)
operator|.
name|getCatalog
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|EntityResolver
name|getResolver
parameter_list|()
block|{
try|try
block|{
name|CatalogManager
name|catalogManager
init|=
operator|new
name|CatalogManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|DEBUG_LEVEL
operator|!=
literal|null
condition|)
block|{
name|catalogManager
operator|.
name|debug
operator|.
name|setDebug
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|DEBUG_LEVEL
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|catalogManager
operator|.
name|setUseStaticCatalog
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|catalogManager
operator|.
name|setIgnoreMissingProperties
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CatalogResolver
name|catalogResolver
init|=
operator|new
name|CatalogResolver
argument_list|(
name|catalogManager
argument_list|)
block|{
specifier|public
name|String
name|getResolvedEntity
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
block|{
name|String
name|s
init|=
name|super
operator|.
name|getResolvedEntity
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
try|try
block|{
name|URIResolver
name|r
init|=
operator|new
name|URIResolver
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|r
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|r
operator|.
name|getURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
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
return|return
name|s
return|;
block|}
block|}
decl_stmt|;
return|return
name|catalogResolver
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|OASISCatalogManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|loadContextCatalogs
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|loadContextCatalogs
parameter_list|()
block|{
name|loadContextCatalogs
argument_list|(
name|DEFAULT_CATALOG_NAME
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|loadContextCatalogs
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|loadCatalogs
argument_list|(
name|getContextClassLoader
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error loading "
operator|+
name|name
operator|+
literal|" catalog files"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|void
name|loadCatalogs
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|catalogs
init|=
name|classLoader
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
decl_stmt|;
while|while
condition|(
name|catalogs
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|URL
name|catalogURL
init|=
name|catalogs
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Catalog found at {0} but no org.apache.xml.resolver.CatalogManager was found."
operator|+
literal|"  Check the classpatch for an xmlresolver jar."
argument_list|,
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|loadedCatalogs
operator|.
name|contains
argument_list|(
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|==
literal|null
condition|)
block|{
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|parseCatalog
argument_list|(
name|catalogURL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|parseCatalog
argument_list|(
name|catalogURL
argument_list|)
expr_stmt|;
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
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|e
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
name|loadedCatalogs
operator|.
name|add
argument_list|(
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|final
name|void
name|loadCatalog
parameter_list|(
name|URL
name|catalogURL
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|loadedCatalogs
operator|.
name|contains
argument_list|(
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|catalogURL
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|catalogURL
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|//just process as is
block|}
block|}
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Catalog found at {0} but no org.apache.xml.resolver.CatalogManager was found."
operator|+
literal|"  Check the classpatch for an xmlresolver jar."
argument_list|,
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|parseCatalog
argument_list|(
name|catalogURL
argument_list|)
expr_stmt|;
name|loadedCatalogs
operator|.
name|add
argument_list|(
name|catalogURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|OASISCatalogManager
name|getContextCatalog
parameter_list|()
block|{
try|try
block|{
name|OASISCatalogManager
name|oasisCatalog
init|=
operator|new
name|OASISCatalogManager
argument_list|()
decl_stmt|;
name|oasisCatalog
operator|.
name|loadContextCatalogs
argument_list|()
expr_stmt|;
return|return
name|oasisCatalog
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|OASISCatalogManager
name|getCatalogManager
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
return|return
name|getContextCatalog
argument_list|()
return|;
block|}
name|OASISCatalogManager
name|catalog
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|OASISCatalogManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
name|catalog
operator|=
name|getContextCatalog
argument_list|()
expr_stmt|;
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|catalog
argument_list|,
name|OASISCatalogManager
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|catalog
return|;
block|}
specifier|public
name|String
name|resolveSystem
parameter_list|(
name|String
name|sys
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
block|{
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|resolveSystem
argument_list|(
name|sys
argument_list|)
return|;
block|}
specifier|public
name|String
name|resolveURI
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
block|{
if|if
condition|(
name|catalog
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|resolveURI
argument_list|(
name|uri
argument_list|)
return|;
block|}
specifier|public
name|String
name|resolvePublic
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|parent
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
block|{
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|Catalog
operator|)
name|catalog
operator|)
operator|.
name|resolvePublic
argument_list|(
name|uri
argument_list|,
name|parent
argument_list|)
return|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|getContextClassLoader
parameter_list|()
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

