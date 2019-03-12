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
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|Iterator
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

begin_class
specifier|public
class|class
name|FileOutputStreamFactory
implements|implements
name|OutputStreamFactory
block|{
name|String
name|dirName
init|=
literal|""
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileNames
decl_stmt|;
name|FileOutputStreamFactory
name|parent
decl_stmt|;
specifier|public
name|FileOutputStreamFactory
parameter_list|()
block|{
name|fileNames
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|FileOutputStreamFactory
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|this
argument_list|(
name|dir
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fileNames
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|FileOutputStreamFactory
parameter_list|(
name|String
name|dir
parameter_list|,
name|FileOutputStreamFactory
name|p
parameter_list|)
block|{
name|dirName
operator|=
name|dir
expr_stmt|;
if|if
condition|(
name|dirName
operator|==
literal|null
condition|)
block|{
name|dirName
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
operator|)
operator|&&
operator|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
operator|!
name|dirName
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|dirName
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dirName
argument_list|)
decl_stmt|;
comment|//create directory
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|parent
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|String
name|getDirectoryName
parameter_list|()
block|{
return|return
name|dirName
return|;
block|}
specifier|private
name|void
name|addFileName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|fileNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|addFileName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getClassDirectory
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|String
name|result
init|=
name|convertPackageNameToDirectory
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
condition|)
block|{
name|result
operator|=
name|dirName
operator|+
name|result
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|String
name|convertPackageNameToDirectory
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|int
name|pos1
init|=
literal|0
decl_stmt|;
name|int
name|pos2
init|=
name|packageName
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|pos1
argument_list|)
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|(
literal|""
argument_list|)
decl_stmt|;
while|while
condition|(
name|pos2
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|packageName
operator|.
name|substring
argument_list|(
name|pos1
argument_list|,
name|pos2
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|File
operator|.
name|separator
argument_list|)
expr_stmt|;
name|pos1
operator|=
name|pos2
operator|+
literal|1
expr_stmt|;
name|pos2
operator|=
name|packageName
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|pos1
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|append
argument_list|(
name|packageName
operator|.
name|substring
argument_list|(
name|pos1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|createFakeOutputStream
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|addFileName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteArrayOutputStream
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|createFakeOutputStream
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|packageDirName
init|=
name|convertPackageNameToDirectory
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|packageDirName
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|packageDirName
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|)
condition|)
block|{
name|packageDirName
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
block|}
name|addFileName
argument_list|(
name|packageDirName
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
operator|new
name|ByteArrayOutputStream
argument_list|()
return|;
block|}
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|packageDirName
init|=
name|convertPackageNameToDirectory
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|packageDirName
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|packageDirName
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|)
condition|)
block|{
name|packageDirName
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
block|}
name|String
name|dname
init|=
name|packageDirName
decl_stmt|;
if|if
condition|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
condition|)
block|{
name|dname
operator|=
name|dirName
operator|+
name|packageDirName
expr_stmt|;
block|}
if|if
condition|(
operator|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|dname
argument_list|)
operator|)
operator|&&
operator|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dname
argument_list|)
operator|)
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dname
argument_list|)
decl_stmt|;
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|addFileName
argument_list|(
name|packageDirName
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
name|Files
operator|.
name|newOutputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|dname
operator|+
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|addFileName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|String
name|dname
init|=
name|name
decl_stmt|;
if|if
condition|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
condition|)
block|{
name|dname
operator|=
name|dirName
operator|+
name|name
expr_stmt|;
block|}
return|return
name|Files
operator|.
name|newOutputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|dname
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|OutputStreamFactory
name|createSubpackageOutputStreamFactory
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|dname
init|=
name|name
decl_stmt|;
if|if
condition|(
operator|!
literal|"."
operator|.
name|equals
argument_list|(
name|dirName
argument_list|)
condition|)
block|{
name|dname
operator|=
name|dirName
operator|+
name|name
expr_stmt|;
block|}
return|return
operator|new
name|FileOutputStreamFactory
argument_list|(
name|dname
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getStreamNames
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|fileNames
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|void
name|clearStreams
parameter_list|()
block|{
name|fileNames
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|clearStreams
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isOutputStreamExists
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|dname
init|=
name|getClassDirectory
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|dname
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|dname
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|)
condition|)
block|{
name|dname
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dname
operator|+
name|name
argument_list|)
decl_stmt|;
return|return
name|file
operator|.
name|exists
argument_list|()
return|;
block|}
block|}
end_class

end_unit

