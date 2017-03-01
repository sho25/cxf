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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
specifier|public
class|class
name|ClassCollector
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|seiClassNames
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typesClassNames
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|exceptionClassNames
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serviceClassNames
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|implClassNames
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clientClassNames
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serverClassNames
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reservedClassNames
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
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|typesPackages
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ClassCollector
parameter_list|()
block|{      }
specifier|public
name|void
name|reserveClass
parameter_list|(
name|String
name|fullName
parameter_list|)
block|{
name|String
name|cls
init|=
name|fullName
decl_stmt|;
name|int
name|idx
init|=
name|cls
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|pkg
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|pkg
operator|=
name|cls
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|cls
operator|=
name|cls
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|reservedClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|)
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addSeiClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addTypesClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addServerClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addImplClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addClientClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addServiceClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
name|addExceptionClassName
argument_list|(
name|pkg
argument_list|,
name|cls
argument_list|,
name|fullName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReserved
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|reservedClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containSeiClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|seiClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containTypesClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|typesClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containExceptionClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|exceptionClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containServiceClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|this
operator|.
name|serviceClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containClientClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|this
operator|.
name|clientClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containServerClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|this
operator|.
name|serverClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containImplClass
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|this
operator|.
name|implClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|addSeiClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|seiClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addTypesClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|typesClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addServerClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|serverClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addImplClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|implClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addClientClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|clientClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addServiceClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|serviceClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExceptionClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|fullClassName
parameter_list|)
block|{
name|exceptionClassNames
operator|.
name|put
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|,
name|fullClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypesFullClassName
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|typesClassNames
operator|.
name|get
argument_list|(
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsTypeIgnoreCase
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|key
init|=
name|key
argument_list|(
name|packagename
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|typesClassNames
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|//try the common fast case first
return|return
literal|true
return|;
block|}
for|for
control|(
name|String
name|s
range|:
name|typesClassNames
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|String
name|key
parameter_list|(
name|String
name|packagename
parameter_list|,
name|String
name|type
parameter_list|)
block|{
return|return
name|packagename
operator|+
literal|"#"
operator|+
name|type
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getTypesPackages
parameter_list|()
block|{
return|return
name|typesPackages
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getGeneratedFileInfo
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|generatedFileList
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|seiClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|typesClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|exceptionClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|serviceClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|implClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|generatedFileList
operator|.
name|addAll
argument_list|(
name|clientClassNames
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|generatedFileList
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getSeiClassNames
parameter_list|()
block|{
return|return
name|seiClassNames
return|;
block|}
specifier|public
name|void
name|setSeiClassNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|seiClassNames
parameter_list|)
block|{
name|this
operator|.
name|seiClassNames
operator|=
name|seiClassNames
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getTypesClassNames
parameter_list|()
block|{
return|return
name|typesClassNames
return|;
block|}
specifier|public
name|void
name|setTypesClassNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typesClassNames
parameter_list|)
block|{
name|this
operator|.
name|typesClassNames
operator|=
name|typesClassNames
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExceptionClassNames
parameter_list|()
block|{
return|return
name|exceptionClassNames
return|;
block|}
specifier|public
name|void
name|setExceptionClassNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|exceptionClassNames
parameter_list|)
block|{
name|this
operator|.
name|exceptionClassNames
operator|=
name|exceptionClassNames
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getServiceClassNames
parameter_list|()
block|{
return|return
name|serviceClassNames
return|;
block|}
specifier|public
name|void
name|setServiceClassNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|serviceClassNames
parameter_list|)
block|{
name|this
operator|.
name|serviceClassNames
operator|=
name|serviceClassNames
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getImplClassNames
parameter_list|()
block|{
return|return
name|implClassNames
return|;
block|}
specifier|public
name|void
name|setImplClassNames
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|implClassNames
parameter_list|)
block|{
name|this
operator|.
name|implClassNames
operator|=
name|implClassNames
expr_stmt|;
block|}
block|}
end_class

end_unit

