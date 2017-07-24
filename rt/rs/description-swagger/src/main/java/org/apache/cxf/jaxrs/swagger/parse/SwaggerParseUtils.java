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
name|swagger
operator|.
name|parse
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Set
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|StringUtils
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
name|CastUtils
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
name|jaxrs
operator|.
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|model
operator|.
name|Parameter
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
name|model
operator|.
name|ParameterType
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
name|model
operator|.
name|UserApplication
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
name|model
operator|.
name|UserOperation
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
name|model
operator|.
name|UserResource
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

begin_class
specifier|public
specifier|final
class|class
name|SwaggerParseUtils
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
name|ResourceUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|SWAGGER_TYPE_MAP
decl_stmt|;
static|static
block|{
name|SWAGGER_TYPE_MAP
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"string"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"integer"
argument_list|,
name|long
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|float
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|double
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|int
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|long
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|byte
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"date"
argument_list|,
name|java
operator|.
name|util
operator|.
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"dateTime"
argument_list|,
name|java
operator|.
name|util
operator|.
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"File"
argument_list|,
name|java
operator|.
name|io
operator|.
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|SWAGGER_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"file"
argument_list|,
name|java
operator|.
name|io
operator|.
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SwaggerParseUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|UserApplication
name|getUserApplication
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
return|return
name|getUserApplication
argument_list|(
name|loc
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplication
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
return|return
name|getUserApplication
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|,
operator|new
name|ParseConfiguration
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplication
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|ParseConfiguration
name|cfg
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getUserApplicationFromStream
argument_list|(
name|is
argument_list|,
name|cfg
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Problem with processing a user model at "
operator|+
name|loc
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplicationFromStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getUserApplicationFromStream
argument_list|(
name|is
argument_list|,
operator|new
name|ParseConfiguration
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplicationFromStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|ParseConfiguration
name|cfg
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getUserApplicationFromJson
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|,
name|cfg
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplicationFromJson
parameter_list|(
name|String
name|json
parameter_list|)
block|{
return|return
name|getUserApplicationFromJson
argument_list|(
name|json
argument_list|,
operator|new
name|ParseConfiguration
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|UserApplication
name|getUserApplicationFromJson
parameter_list|(
name|String
name|json
parameter_list|,
name|ParseConfiguration
name|cfg
parameter_list|)
block|{
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|reader
operator|.
name|fromJson
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|UserApplication
name|app
init|=
operator|new
name|UserApplication
argument_list|()
decl_stmt|;
name|String
name|relativePath
init|=
operator|(
name|String
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"basePath"
argument_list|)
decl_stmt|;
name|app
operator|.
name|setBasePath
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|relativePath
argument_list|)
condition|?
literal|"/"
else|:
name|relativePath
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|UserOperation
argument_list|>
argument_list|>
name|userOpsMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|UserOperation
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|tags
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|tagsProp
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"tags"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|tagsProp
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|tagProp
range|:
name|tagsProp
control|)
block|{
name|tags
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|tagProp
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|tags
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|tag
range|:
name|tags
control|)
block|{
name|userOpsMap
operator|.
name|put
argument_list|(
name|tag
argument_list|,
operator|new
name|LinkedList
argument_list|<
name|UserOperation
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|paths
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"paths"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|pathEntry
range|:
name|paths
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|operPath
init|=
name|pathEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|operations
init|=
name|pathEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|operEntry
range|:
name|operations
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|UserOperation
name|userOp
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|userOp
operator|.
name|setVerb
argument_list|(
name|operEntry
operator|.
name|getKey
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|oper
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|operEntry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|opTags
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|oper
operator|.
name|get
argument_list|(
literal|"tags"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|opTag
init|=
name|opTags
operator|==
literal|null
condition|?
literal|""
else|:
name|opTags
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|realOpPath
init|=
name|operPath
operator|.
name|equals
argument_list|(
literal|"/"
operator|+
name|opTag
argument_list|)
condition|?
literal|"/"
else|:
name|operPath
operator|.
name|substring
argument_list|(
name|opTag
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|userOp
operator|.
name|setPath
argument_list|(
name|realOpPath
argument_list|)
expr_stmt|;
name|userOp
operator|.
name|setName
argument_list|(
operator|(
name|String
operator|)
name|oper
operator|.
name|get
argument_list|(
literal|"operationId"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|opProduces
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|oper
operator|.
name|get
argument_list|(
literal|"produces"
argument_list|)
argument_list|)
decl_stmt|;
name|userOp
operator|.
name|setProduces
argument_list|(
name|listToString
argument_list|(
name|opProduces
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|opConsumes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|oper
operator|.
name|get
argument_list|(
literal|"consumes"
argument_list|)
argument_list|)
decl_stmt|;
name|userOp
operator|.
name|setConsumes
argument_list|(
name|listToString
argument_list|(
name|opConsumes
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Parameter
argument_list|>
name|userOpParams
init|=
operator|new
name|LinkedList
argument_list|<
name|Parameter
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|params
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|oper
operator|.
name|get
argument_list|(
literal|"parameters"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
range|:
name|params
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|param
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
comment|//"query", "header", "path", "formData" or "body"
name|String
name|paramType
init|=
operator|(
name|String
operator|)
name|param
operator|.
name|get
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|ParameterType
name|pType
init|=
literal|"body"
operator|.
name|equals
argument_list|(
name|paramType
argument_list|)
condition|?
name|ParameterType
operator|.
name|REQUEST_BODY
else|:
literal|"formData"
operator|.
name|equals
argument_list|(
name|paramType
argument_list|)
condition|?
name|ParameterType
operator|.
name|FORM
else|:
name|ParameterType
operator|.
name|valueOf
argument_list|(
name|paramType
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
name|Parameter
name|userParam
init|=
operator|new
name|Parameter
argument_list|(
name|pType
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|setJavaType
argument_list|(
name|userParam
argument_list|,
operator|(
name|String
operator|)
name|param
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|userOpParams
operator|.
name|add
argument_list|(
name|userParam
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|userOpParams
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|userOp
operator|.
name|setParameters
argument_list|(
name|userOpParams
argument_list|)
expr_stmt|;
block|}
name|userOpsMap
operator|.
name|get
argument_list|(
name|opTag
argument_list|)
operator|.
name|add
argument_list|(
name|userOp
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
init|=
operator|new
name|LinkedList
argument_list|<
name|UserResource
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|UserOperation
argument_list|>
argument_list|>
name|entry
range|:
name|userOpsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|UserResource
name|ur
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|ur
operator|.
name|setPath
argument_list|(
literal|"/"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|ur
operator|.
name|setOperations
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|ur
operator|.
name|setName
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|resources
operator|.
name|add
argument_list|(
name|ur
argument_list|)
expr_stmt|;
block|}
name|app
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
return|return
name|app
return|;
block|}
specifier|private
specifier|static
name|void
name|setJavaType
parameter_list|(
name|Parameter
name|userParam
parameter_list|,
name|String
name|typeName
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|javaType
init|=
name|SWAGGER_TYPE_MAP
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaType
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// May work if the model has already been compiled
comment|// TODO: need to know the package name
name|javaType
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|typeName
argument_list|,
name|SwaggerParseUtils
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|userParam
operator|.
name|setJavaType
argument_list|(
name|javaType
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|listToString
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

