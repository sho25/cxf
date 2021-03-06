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
name|bus
operator|.
name|spring
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
name|Properties
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
name|ConcurrentHashMap
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|DelegatingEntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|PropertiesLoaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|CollectionUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|BusEntityResolver
extends|extends
name|DelegatingEntityResolver
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
name|BusEntityResolver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|EntityResolver
name|dtdResolver
decl_stmt|;
specifier|private
name|EntityResolver
name|schemaResolver
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|schemaMappings
decl_stmt|;
specifier|private
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|public
name|BusEntityResolver
parameter_list|(
name|ClassLoader
name|loader
parameter_list|,
name|EntityResolver
name|dr
parameter_list|,
name|EntityResolver
name|sr
parameter_list|)
block|{
name|super
argument_list|(
name|dr
argument_list|,
name|sr
argument_list|)
expr_stmt|;
name|classLoader
operator|=
name|loader
expr_stmt|;
name|dtdResolver
operator|=
name|dr
expr_stmt|;
name|schemaResolver
operator|=
name|sr
expr_stmt|;
try|try
block|{
name|Properties
name|mappings
init|=
name|PropertiesLoaderUtils
operator|.
name|loadAllProperties
argument_list|(
literal|"META-INF/spring.schemas"
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
name|schemaMappings
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|mappings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|mergePropertiesIntoMap
argument_list|(
name|mappings
argument_list|,
name|schemaMappings
argument_list|)
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
annotation|@
name|Override
specifier|public
name|InputSource
name|resolveEntity
parameter_list|(
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
block|{
name|InputSource
name|source
init|=
name|super
operator|.
name|resolveEntity
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|source
operator|&&
literal|null
operator|!=
name|systemId
condition|)
block|{
comment|// try the schema and dtd resolver in turn, ignoring the suffix in publicId
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Attempting to resolve systemId {0}"
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
name|source
operator|=
name|schemaResolver
operator|.
name|resolveEntity
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|source
condition|)
block|{
name|source
operator|=
name|dtdResolver
operator|.
name|resolveEntity
argument_list|(
name|publicId
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|source
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
name|String
name|resourceLocation
init|=
name|schemaMappings
operator|.
name|get
argument_list|(
name|systemId
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourceLocation
operator|!=
literal|null
operator|&&
name|publicId
operator|==
literal|null
condition|)
block|{
name|Resource
name|resource
init|=
operator|new
name|ClassPathResource
argument_list|(
name|resourceLocation
argument_list|,
name|classLoader
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|.
name|exists
argument_list|()
condition|)
block|{
name|source
operator|.
name|setPublicId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
name|source
operator|.
name|setSystemId
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

