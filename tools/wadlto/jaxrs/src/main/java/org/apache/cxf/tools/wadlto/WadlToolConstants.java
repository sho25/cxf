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
name|wadlto
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolConstants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WadlToolConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CFG_OUTPUTDIR
init|=
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_COMPILE
init|=
name|ToolConstants
operator|.
name|CFG_COMPILE
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CLASSDIR
init|=
name|ToolConstants
operator|.
name|CFG_CLASSDIR
decl_stmt|;
comment|/**      * Default      */
specifier|public
specifier|static
specifier|final
name|String
name|CFG_INTERFACE
init|=
name|ToolConstants
operator|.
name|CFG_INTERFACE
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_IMPL
init|=
name|ToolConstants
operator|.
name|CFG_IMPL
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_TYPES
init|=
name|ToolConstants
operator|.
name|CFG_TYPES
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_PACKAGENAME
init|=
name|ToolConstants
operator|.
name|CFG_PACKAGENAME
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SCHEMA_PACKAGENAME
init|=
literal|"schemaPackagename"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_RESOURCENAME
init|=
literal|"resourcename"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SCHEMA_TYPE_MAP
init|=
literal|"schemaTypeMap"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MEDIA_TYPE_MAP
init|=
literal|"mediaTypeMap"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_MULTIPLE_XML_REPS
init|=
literal|"supportMultipleXmlReps"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_CATALOG
init|=
name|ToolConstants
operator|.
name|CFG_CATALOG
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_BINDING
init|=
name|ToolConstants
operator|.
name|CFG_BINDING
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NO_TYPES
init|=
name|ToolConstants
operator|.
name|CFG_NO_TYPES
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NO_VOID_FOR_EMPTY_RESPONSES
init|=
literal|"noVoidForEmptyResponses"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_NO_ADDRESS_BINDING
init|=
name|ToolConstants
operator|.
name|CFG_NO_ADDRESS_BINDING
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WADL_NAMESPACE
init|=
literal|"wadlNamespace"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_GENERATE_ENUMS
init|=
literal|"generateEnums"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CFG_INHERIT_PARAMS
init|=
literal|"inheritResourceParams"
decl_stmt|;
comment|// JAX-RS 2.0 @Suspended AsyncResponse
specifier|public
specifier|static
specifier|final
name|String
name|CFG_SUSPENDED_ASYNC
init|=
literal|"async"
decl_stmt|;
comment|// CXF @UseAsyncMethod - equivalent of the proposed @ManagedAsync which
comment|// which did not make it into JAX-RS 2.0
comment|//public static final String CFG_MANAGED_ASYNC = "managedAsync";
specifier|public
specifier|static
specifier|final
name|String
name|CFG_WADLURL
init|=
literal|"wadl"
decl_stmt|;
specifier|private
name|WadlToolConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

