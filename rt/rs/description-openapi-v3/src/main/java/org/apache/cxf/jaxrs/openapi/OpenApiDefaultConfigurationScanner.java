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
name|openapi
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|tuple
operator|.
name|ImmutablePair
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|integration
operator|.
name|ClasspathOpenApiConfigurationLoader
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|integration
operator|.
name|FileOpenApiConfigurationLoader
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|integration
operator|.
name|api
operator|.
name|OpenApiConfigurationLoader
import|;
end_import

begin_comment
comment|/**  * Scans a set of known configuration locations in order to locate the OpenAPI   * configuration.   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OpenApiDefaultConfigurationScanner
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|OpenApiConfigurationLoader
argument_list|>
name|LOADERS
init|=
name|getLocationLoaders
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|ImmutablePair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|KNOWN_LOCATIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"classpath"
argument_list|,
literal|"openapi-configuration.yaml"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"classpath"
argument_list|,
literal|"openapi-configuration.json"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"classpath"
argument_list|,
literal|"openapi.yaml"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"classpath"
argument_list|,
literal|"openapi.json"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"file"
argument_list|,
literal|"openapi-configuration.yaml"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"file"
argument_list|,
literal|"openapi-configuration.json"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"file"
argument_list|,
literal|"openapi.yaml"
argument_list|)
argument_list|,
operator|new
name|ImmutablePair
argument_list|<>
argument_list|(
literal|"file"
argument_list|,
literal|"openapi.json"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|OpenApiDefaultConfigurationScanner
parameter_list|()
block|{     }
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|OpenApiConfigurationLoader
argument_list|>
name|getLocationLoaders
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|OpenApiConfigurationLoader
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"classpath"
argument_list|,
operator|new
name|ClasspathOpenApiConfigurationLoader
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"file"
argument_list|,
operator|new
name|FileOpenApiConfigurationLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|locateDefaultConfiguration
parameter_list|()
block|{
return|return
name|KNOWN_LOCATIONS
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|location
lambda|->
name|LOADERS
operator|.
name|get
argument_list|(
name|location
operator|.
name|left
argument_list|)
operator|.
name|exists
argument_list|(
name|location
operator|.
name|right
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|map
argument_list|(
name|ImmutablePair
operator|::
name|getValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

