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
name|maven_plugin
operator|.
name|wadlto
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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|Option
block|{
comment|/**      * Directory where generated java classes will be created. Defaults to plugin 'sourceRoot' parameter      */
specifier|protected
name|File
name|outputDir
decl_stmt|;
comment|/**      * A set of dependent files used to detect the generator must process WSDL, even       * if generator marker files are up to date.      */
name|File
name|dependencies
index|[]
decl_stmt|;
comment|/**      * Redundant directories to be deleted after code generation      */
name|File
name|redundantDirs
index|[]
decl_stmt|;
comment|/**      * Extra arguments to pass to the command-line code generator. For compatibility as well as to      * specify any extra flags not addressed by other parameters      */
name|List
argument_list|<
name|String
argument_list|>
name|extraargs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Specifies JAXB binding files. Use spaces to separate multiple entries.      */
name|String
name|bindingFiles
index|[]
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
comment|/**      * Specifies catalog file to map the imported wadl/schema      */
name|String
name|catalog
decl_stmt|;
comment|/**      * Specifies resource id      */
specifier|private
name|String
name|resourcename
decl_stmt|;
comment|/**      * Specifies package name of WADL resource elements       */
specifier|private
name|String
name|packagename
decl_stmt|;
comment|/**      * Enables or disables generation of the impl classes. Default value is false.      * If set then only implementation classes will be generated      */
specifier|private
name|Boolean
name|generateImpl
decl_stmt|;
comment|/**      * Enables or disables generation of the interface classes. Setting this property      * only makes sense when generateImpl is also set. In other cases it is ignored and      * interfaces are always generated.      *        *       *       */
specifier|private
name|Boolean
name|generateInterface
decl_stmt|;
comment|/**      *       */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaPackagenames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Option
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setDependencies
parameter_list|(
name|File
name|files
index|[]
parameter_list|)
block|{
name|dependencies
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|File
index|[]
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|void
name|setDeleteDirs
parameter_list|(
name|File
name|files
index|[]
parameter_list|)
block|{
name|redundantDirs
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|File
index|[]
name|getDeleteDirs
parameter_list|()
block|{
return|return
name|redundantDirs
return|;
block|}
specifier|public
name|File
name|getOutputDir
parameter_list|()
block|{
return|return
name|outputDir
return|;
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|outputDir
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|void
name|setBindingFiles
parameter_list|(
name|String
name|files
index|[]
parameter_list|)
block|{
name|bindingFiles
operator|=
name|files
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getBindingFiles
parameter_list|()
block|{
return|return
name|bindingFiles
return|;
block|}
specifier|public
name|void
name|addBindingFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|tmp
index|[]
init|=
operator|new
name|String
index|[
name|bindingFiles
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bindingFiles
argument_list|,
literal|0
argument_list|,
name|tmp
argument_list|,
literal|0
argument_list|,
name|bindingFiles
operator|.
name|length
argument_list|)
expr_stmt|;
name|bindingFiles
operator|=
name|tmp
expr_stmt|;
name|bindingFiles
index|[
name|bindingFiles
operator|.
name|length
operator|-
literal|1
index|]
operator|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaPackagenames
parameter_list|()
block|{
return|return
name|schemaPackagenames
return|;
block|}
specifier|public
name|void
name|setSchemaPackagenames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|pn
parameter_list|)
block|{
name|this
operator|.
name|schemaPackagenames
operator|=
name|pn
expr_stmt|;
block|}
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
specifier|public
name|String
name|getPackagename
parameter_list|()
block|{
return|return
name|packagename
return|;
block|}
specifier|public
name|void
name|setPackagename
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|packagename
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setResourcename
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
name|this
operator|.
name|resourcename
operator|=
name|resourceName
expr_stmt|;
block|}
specifier|public
name|String
name|getResourcename
parameter_list|()
block|{
return|return
name|resourcename
return|;
block|}
specifier|public
name|boolean
name|isImpl
parameter_list|()
block|{
return|return
name|generateImpl
operator|==
literal|null
condition|?
literal|false
else|:
name|generateImpl
return|;
block|}
specifier|public
name|void
name|setImpl
parameter_list|(
name|boolean
name|impl
parameter_list|)
block|{
name|this
operator|.
name|generateImpl
operator|=
name|impl
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInterface
parameter_list|()
block|{
return|return
name|generateInterface
operator|==
literal|null
condition|?
literal|false
else|:
name|generateInterface
return|;
block|}
specifier|public
name|void
name|setInterface
parameter_list|(
name|boolean
name|interf
parameter_list|)
block|{
name|this
operator|.
name|generateInterface
operator|=
name|interf
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExtraargs
parameter_list|()
block|{
return|return
name|extraargs
return|;
block|}
specifier|public
name|void
name|setExtraargs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ea
parameter_list|)
block|{
name|this
operator|.
name|extraargs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraargs
operator|.
name|addAll
argument_list|(
name|ea
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|copyOptions
parameter_list|(
name|Option
name|destination
parameter_list|)
block|{
name|destination
operator|.
name|setBindingFiles
argument_list|(
name|getBindingFiles
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setCatalog
argument_list|(
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setResourcename
argument_list|(
name|getResourcename
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setSchemaPackagenames
argument_list|(
name|getSchemaPackagenames
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDeleteDirs
argument_list|(
name|getDeleteDirs
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setDependencies
argument_list|(
name|getDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setOutputDir
argument_list|(
name|getOutputDir
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|setExtraargs
argument_list|(
name|getExtraargs
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|setIfNull
parameter_list|(
name|T
name|dest
parameter_list|,
name|T
name|source
parameter_list|)
block|{
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|dest
operator|=
name|source
expr_stmt|;
block|}
return|return
name|dest
return|;
block|}
specifier|public
name|void
name|merge
parameter_list|(
name|Option
name|defaultOptions
parameter_list|)
block|{
name|catalog
operator|=
name|setIfNull
argument_list|(
name|catalog
argument_list|,
name|defaultOptions
operator|.
name|catalog
argument_list|)
expr_stmt|;
name|generateImpl
operator|=
name|setIfNull
argument_list|(
name|generateImpl
argument_list|,
name|defaultOptions
operator|.
name|generateImpl
argument_list|)
expr_stmt|;
name|generateInterface
operator|=
name|setIfNull
argument_list|(
name|generateInterface
argument_list|,
name|defaultOptions
operator|.
name|generateInterface
argument_list|)
expr_stmt|;
name|packagename
operator|=
name|setIfNull
argument_list|(
name|packagename
argument_list|,
name|defaultOptions
operator|.
name|packagename
argument_list|)
expr_stmt|;
name|outputDir
operator|=
name|setIfNull
argument_list|(
name|outputDir
argument_list|,
name|defaultOptions
operator|.
name|outputDir
argument_list|)
expr_stmt|;
name|bindingFiles
operator|=
name|mergeList
argument_list|(
name|bindingFiles
argument_list|,
name|defaultOptions
operator|.
name|bindingFiles
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|dependencies
operator|=
name|mergeList
argument_list|(
name|dependencies
argument_list|,
name|defaultOptions
operator|.
name|dependencies
argument_list|,
name|File
operator|.
name|class
argument_list|)
expr_stmt|;
name|redundantDirs
operator|=
name|mergeList
argument_list|(
name|redundantDirs
argument_list|,
name|defaultOptions
operator|.
name|redundantDirs
argument_list|,
name|File
operator|.
name|class
argument_list|)
expr_stmt|;
name|schemaPackagenames
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|schemaPackagenames
argument_list|)
expr_stmt|;
name|extraargs
operator|.
name|addAll
argument_list|(
name|defaultOptions
operator|.
name|extraargs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|mergeList
parameter_list|(
name|T
index|[]
name|l1
parameter_list|,
name|T
index|[]
name|l2
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|l1
operator|==
literal|null
condition|)
block|{
return|return
name|l2
return|;
block|}
elseif|else
if|if
condition|(
name|l2
operator|==
literal|null
condition|)
block|{
return|return
name|l1
return|;
block|}
name|int
name|len
init|=
name|l1
operator|.
name|length
operator|+
name|l2
operator|.
name|length
decl_stmt|;
name|T
name|ret
index|[]
init|=
operator|(
name|T
index|[]
operator|)
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
operator|.
name|newInstance
argument_list|(
name|cls
argument_list|,
name|len
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|l1
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
literal|0
argument_list|,
name|l1
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|l2
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
name|l1
operator|.
name|length
argument_list|,
name|l2
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

