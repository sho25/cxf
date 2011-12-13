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
name|wsdl2js
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
name|net
operator|.
name|URI
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|maven_plugin
operator|.
name|WsdlArtifact
import|;
end_import

begin_comment
comment|/**  * An option for javascript generation.  */
end_comment

begin_class
specifier|public
class|class
name|WsdlOption
extends|extends
name|Option
implements|implements
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|maven_plugin
operator|.
name|GenericWsdlOption
block|{
specifier|private
name|String
name|wsdl
decl_stmt|;
specifier|private
name|WsdlArtifact
name|artifact
decl_stmt|;
comment|/**      * @return Pathname or URI to wsdl.      */
specifier|public
name|String
name|getWsdl
parameter_list|()
block|{
return|return
name|wsdl
return|;
block|}
comment|/**      * Set pathname or URI to WSDL.      * @param wsdl path.      */
specifier|public
name|void
name|setWsdl
parameter_list|(
name|String
name|wsdl
parameter_list|)
block|{
name|this
operator|.
name|wsdl
operator|=
name|wsdl
expr_stmt|;
block|}
comment|/**      * Maven coordinates       * @return      */
specifier|public
name|WsdlArtifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|setArtifact
parameter_list|(
name|WsdlArtifact
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|wsdl
return|;
block|}
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|wsdl
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|File
index|[]
name|getDeleteDirs
parameter_list|()
block|{
comment|/*          * Until we figure out what this amounts to. I suspect it stays null for Javascript.          */
return|return
operator|new
name|File
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|generateCommandLine
parameter_list|(
name|File
name|outputDirFile
parameter_list|,
name|URI
name|basedir
parameter_list|,
name|URI
name|wsdlURI
parameter_list|,
name|boolean
name|debug
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsdlVersion
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|wsdlVersion
argument_list|)
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
literal|"-wv"
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|wsdlVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|packagePrefixes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|UriPrefixPair
name|upp
range|:
name|packagePrefixes
control|)
block|{
name|options
operator|.
name|add
argument_list|(
literal|"-p"
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s=%s"
argument_list|,
name|upp
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|upp
operator|.
name|getUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|catalog
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|catalog
argument_list|)
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
literal|"-catalog"
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|catalog
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|options
operator|.
name|add
argument_list|(
literal|"-d"
argument_list|)
expr_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
name|output
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|options
operator|.
name|add
argument_list|(
name|outputDirFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|validate
operator|!=
literal|null
operator|&&
name|validate
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
literal|"-validate"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|debug
condition|)
block|{
name|options
operator|.
name|add
argument_list|(
literal|"-v"
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
literal|"-V"
argument_list|)
expr_stmt|;
block|}
comment|/*          * By the time we get here there's supposed to be a string          * in 'wsdl' that we can use as the uri.          */
name|options
operator|.
name|add
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

