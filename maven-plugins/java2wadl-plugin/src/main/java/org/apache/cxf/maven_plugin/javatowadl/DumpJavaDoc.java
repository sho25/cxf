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
name|javatowadl
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|ClassDoc
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|DocErrorReporter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|MethodDoc
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|ParamTag
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|RootDoc
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|javadoc
operator|.
name|Tag
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|DumpJavaDoc
block|{
specifier|private
name|DumpJavaDoc
parameter_list|()
block|{      }
specifier|public
specifier|static
name|boolean
name|start
parameter_list|(
name|RootDoc
name|root
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|dumpFileName
init|=
name|readOptions
argument_list|(
name|root
operator|.
name|options
argument_list|()
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|dumpFileName
argument_list|)
argument_list|)
decl_stmt|;
name|Properties
name|javaDocMap
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassDoc
name|classDoc
range|:
name|root
operator|.
name|classes
argument_list|()
control|)
block|{
name|javaDocMap
operator|.
name|put
argument_list|(
name|classDoc
operator|.
name|toString
argument_list|()
argument_list|,
name|classDoc
operator|.
name|commentText
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|MethodDoc
name|method
range|:
name|classDoc
operator|.
name|methods
argument_list|()
control|)
block|{
name|javaDocMap
operator|.
name|put
argument_list|(
name|method
operator|.
name|qualifiedName
argument_list|()
argument_list|,
name|method
operator|.
name|commentText
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ParamTag
name|paramTag
range|:
name|method
operator|.
name|paramTags
argument_list|()
control|)
block|{
name|Parameter
index|[]
name|parameters
init|=
name|method
operator|.
name|parameters
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parameters
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|parameters
index|[
name|i
index|]
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|paramTag
operator|.
name|parameterName
argument_list|()
argument_list|)
condition|)
block|{
name|javaDocMap
operator|.
name|put
argument_list|(
name|method
operator|.
name|qualifiedName
argument_list|()
operator|+
literal|".paramCommentTag."
operator|+
name|i
argument_list|,
name|paramTag
operator|.
name|parameterComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Tag
index|[]
name|retTags
init|=
name|method
operator|.
name|tags
argument_list|(
literal|"return"
argument_list|)
decl_stmt|;
if|if
condition|(
name|retTags
operator|!=
literal|null
operator|&&
name|retTags
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|Tag
name|retTag
init|=
name|method
operator|.
name|tags
argument_list|(
literal|"return"
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|javaDocMap
operator|.
name|put
argument_list|(
name|method
operator|.
name|qualifiedName
argument_list|()
operator|+
literal|"."
operator|+
literal|"returnCommentTag"
argument_list|,
name|retTag
operator|.
name|text
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|javaDocMap
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|String
name|readOptions
parameter_list|(
name|String
index|[]
index|[]
name|options
parameter_list|)
block|{
name|String
name|tagName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|options
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
index|[]
name|opt
init|=
name|options
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
literal|"-dumpJavaDocFile"
operator|.
name|equals
argument_list|(
name|opt
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|tagName
operator|=
name|opt
index|[
literal|1
index|]
expr_stmt|;
block|}
block|}
return|return
name|tagName
return|;
block|}
specifier|public
specifier|static
name|int
name|optionLength
parameter_list|(
name|String
name|option
parameter_list|)
block|{
if|if
condition|(
literal|"-dumpJavaDocFile"
operator|.
name|equals
argument_list|(
name|option
argument_list|)
condition|)
block|{
return|return
literal|2
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|public
specifier|static
name|boolean
name|validOptions
parameter_list|(
name|String
index|[]
index|[]
name|options
parameter_list|,
name|DocErrorReporter
name|reporter
parameter_list|)
block|{
name|boolean
name|foundTagOption
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|options
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
index|[]
name|opt
init|=
name|options
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
literal|"-dumpJavaDocFile"
operator|.
name|equals
argument_list|(
name|opt
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
if|if
condition|(
name|foundTagOption
condition|)
block|{
name|reporter
operator|.
name|printError
argument_list|(
literal|"Only one -dumpJavaDocFile option allowed."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|foundTagOption
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|foundTagOption
condition|)
block|{
name|reporter
operator|.
name|printError
argument_list|(
literal|"Usage: -dumpJavaDocFile theFileToDumpJavaDocForLatarUse..."
argument_list|)
expr_stmt|;
block|}
return|return
name|foundTagOption
return|;
block|}
block|}
end_class

end_unit

