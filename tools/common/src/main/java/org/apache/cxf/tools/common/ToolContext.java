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
name|common
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
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|common
operator|.
name|util
operator|.
name|PackageUtils
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
name|tools
operator|.
name|common
operator|.
name|model
operator|.
name|JavaModel
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
name|tools
operator|.
name|util
operator|.
name|PropertyUtil
import|;
end_import

begin_class
specifier|public
class|class
name|ToolContext
block|{
specifier|protected
name|JavaModel
name|javaModel
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|paramMap
decl_stmt|;
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|private
name|boolean
name|packageNameChanged
decl_stmt|;
specifier|private
name|ToolErrorListener
name|errors
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePackageMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|excludeNamespacePackageMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|InputSource
argument_list|>
name|jaxbBindingFiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|excludePkgList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|excludeFileList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ToolContext
parameter_list|()
block|{     }
specifier|public
name|void
name|loadDefaultNS2Pck
parameter_list|(
name|InputStream
name|ins
parameter_list|)
block|{
try|try
block|{
name|PropertyUtil
name|properties
init|=
operator|new
name|PropertyUtil
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|namespacePackageMap
operator|.
name|putAll
argument_list|(
name|properties
operator|.
name|getMaps
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|loadDefaultExcludes
parameter_list|(
name|InputStream
name|ins
parameter_list|)
block|{
try|try
block|{
name|PropertyUtil
name|properties
init|=
operator|new
name|PropertyUtil
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|namespacePackageMap
operator|.
name|putAll
argument_list|(
name|properties
operator|.
name|getMaps
argument_list|()
argument_list|)
expr_stmt|;
name|excludeNamespacePackageMap
operator|.
name|putAll
argument_list|(
name|properties
operator|.
name|getMaps
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|JavaModel
name|getJavaModel
parameter_list|()
block|{
return|return
name|javaModel
return|;
block|}
specifier|public
name|void
name|setJavaModel
parameter_list|(
name|JavaModel
name|jModel
parameter_list|)
block|{
name|this
operator|.
name|javaModel
operator|=
name|jModel
expr_stmt|;
block|}
specifier|public
name|void
name|addParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
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
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|optionSet
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|paramMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|paramMap
operator|!=
literal|null
operator|)
operator|&&
name|paramMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|paramMap
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|paramMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getArray
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|Object
name|o
init|=
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
operator|(
name|String
operator|)
name|o
block|}
return|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|o
return|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
operator|!
name|optionSet
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * avoid need to suppress warnings on string->object cases.      * @param<T>      * @param key      * @param clazz      * @return      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|get
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
block|{
return|return
name|key
operator|.
name|cast
argument_list|(
name|get
argument_list|(
name|key
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|put
argument_list|(
name|key
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getBooleanValue
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|get
argument_list|(
name|key
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|paramMap
operator|==
literal|null
condition|)
block|{
name|paramMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|paramMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|paramMap
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|paramMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|optionSet
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|null
operator|)
condition|?
literal|false
else|:
literal|true
return|;
block|}
specifier|public
name|boolean
name|isVerbose
parameter_list|()
block|{
name|String
name|verboseProperty
init|=
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_VERBOSE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|verboseProperty
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|ToolConstants
operator|.
name|CFG_VERBOSE
operator|.
name|equals
argument_list|(
name|verboseProperty
argument_list|)
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|verboseProperty
argument_list|)
return|;
block|}
comment|// REVIST: Prefer using optionSet, to keep the context clean
specifier|public
name|boolean
name|fullValidateWSDL
parameter_list|()
block|{
name|Object
name|s
init|=
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_VALIDATE_WSDL
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'='
condition|)
block|{
name|s
operator|=
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
operator|(
name|s
operator|==
literal|null
operator|||
literal|"none"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"basic"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|)
return|;
block|}
specifier|public
name|boolean
name|basicValidateWSDL
parameter_list|()
block|{
name|Object
name|s
init|=
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_VALIDATE_WSDL
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'='
condition|)
block|{
name|s
operator|=
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
operator|(
literal|"none"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
operator|)
return|;
block|}
specifier|public
name|void
name|addNamespacePackageMap
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|pn
parameter_list|)
block|{
name|this
operator|.
name|namespacePackageMap
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|pn
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|mapNamespaceToPackageName
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|this
operator|.
name|namespacePackageMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|this
operator|.
name|namespacePackageMap
operator|.
name|containsKey
argument_list|(
name|ns
argument_list|)
return|;
block|}
specifier|public
name|void
name|addExcludeNamespacePackageMap
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|pn
parameter_list|)
block|{
name|excludeNamespacePackageMap
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|pn
argument_list|)
expr_stmt|;
name|excludePkgList
operator|.
name|add
argument_list|(
name|pn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasExcludeNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|excludeNamespacePackageMap
operator|.
name|containsKey
argument_list|(
name|ns
argument_list|)
return|;
block|}
specifier|public
name|String
name|getExcludePackageName
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|this
operator|.
name|excludeNamespacePackageMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPackageName
parameter_list|(
name|String
name|pkgName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|pkgName
expr_stmt|;
name|packageNameChanged
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|this
operator|.
name|packageName
return|;
block|}
specifier|public
name|String
name|mapPackageName
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|ns
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|hasNamespace
argument_list|(
name|ns
argument_list|)
condition|)
block|{
return|return
name|mapNamespaceToPackageName
argument_list|(
name|ns
argument_list|)
return|;
block|}
if|if
condition|(
name|getPackageName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getPackageName
argument_list|()
return|;
block|}
return|return
name|PackageUtils
operator|.
name|parsePackageName
argument_list|(
name|ns
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|getCustomizedNS
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|PackageUtils
operator|.
name|getNamespace
argument_list|(
name|mapPackageName
argument_list|(
name|ns
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJaxbBindingFiles
parameter_list|(
name|List
argument_list|<
name|InputSource
argument_list|>
name|bindings
parameter_list|)
block|{
name|jaxbBindingFiles
operator|=
name|bindings
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|InputSource
argument_list|>
name|getJaxbBindingFile
parameter_list|()
block|{
return|return
name|this
operator|.
name|jaxbBindingFiles
return|;
block|}
specifier|public
name|boolean
name|isExcludeNamespaceEnabled
parameter_list|()
block|{
return|return
operator|!
name|excludeNamespacePackageMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludePkgList
parameter_list|()
block|{
return|return
name|this
operator|.
name|excludePkgList
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludeFileList
parameter_list|()
block|{
return|return
name|this
operator|.
name|excludeFileList
return|;
block|}
specifier|public
name|QName
name|getQName
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|getQName
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getQName
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
if|if
condition|(
name|optionSet
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|String
name|pns
init|=
operator|(
name|String
operator|)
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|pos
init|=
name|pns
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
name|String
name|localname
init|=
name|pns
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|ns
init|=
name|pns
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
name|localname
operator|=
name|pns
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|localname
argument_list|)
return|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|defaultNamespace
argument_list|,
name|localname
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ToolErrorListener
name|getErrorListener
parameter_list|()
block|{
if|if
condition|(
name|errors
operator|==
literal|null
condition|)
block|{
name|errors
operator|=
operator|new
name|ToolErrorListener
argument_list|()
expr_stmt|;
block|}
return|return
name|errors
return|;
block|}
specifier|public
name|void
name|setErrorListener
parameter_list|(
name|ToolErrorListener
name|e
parameter_list|)
block|{
name|errors
operator|=
name|e
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespacePackageMap
parameter_list|()
block|{
return|return
name|namespacePackageMap
return|;
block|}
specifier|public
name|boolean
name|isPackageNameChanged
parameter_list|()
block|{
return|return
name|packageNameChanged
return|;
block|}
comment|/**      * This method attempts to do a deep copy of items which may change in this ToolContext.      * The intent of this is to be able to take a snapshot of the state of the ToolContext      * after it's initialised so we can run a tool multiple times with the same setup      * while not having the state preserved between multiple runs. I didn't want      * to call this clone() as it neither does a deep nor shallow copy. It does a mix      * based on my best guess at what changes and what doesn't.      */
specifier|public
name|ToolContext
name|makeCopy
parameter_list|()
block|{
name|ToolContext
name|newCopy
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
name|newCopy
operator|.
name|javaModel
operator|=
name|javaModel
expr_stmt|;
name|newCopy
operator|.
name|paramMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|paramMap
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|packageName
operator|=
name|packageName
expr_stmt|;
name|newCopy
operator|.
name|packageNameChanged
operator|=
name|packageNameChanged
expr_stmt|;
name|newCopy
operator|.
name|namespacePackageMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|namespacePackageMap
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|excludeNamespacePackageMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|excludeNamespacePackageMap
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|jaxbBindingFiles
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|jaxbBindingFiles
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|excludePkgList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|excludePkgList
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|excludeFileList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|excludeFileList
argument_list|)
expr_stmt|;
name|newCopy
operator|.
name|errors
operator|=
name|errors
expr_stmt|;
return|return
name|newCopy
return|;
block|}
block|}
end_class

end_unit

