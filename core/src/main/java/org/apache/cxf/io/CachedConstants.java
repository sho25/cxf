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
name|io
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|CachedConstants
block|{
comment|/**      * The directory name for storing the temporary files. None is specified by default.      */
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_DIRECTORY_SYS_PROP
init|=
literal|"org.apache.cxf.io.CachedOutputStream.OutputDirectory"
decl_stmt|;
comment|/**      * The threshold value in bytes to switch from memory to file caching. The default value is 128K for      * CachedOutputStream and 64K for CachedWriter.      */
specifier|public
specifier|static
specifier|final
name|String
name|THRESHOLD_SYS_PROP
init|=
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
decl_stmt|;
comment|/**      * The threshold value in bytes to switch from memory to file caching. The default value is 128K for      * CachedOutputStream and 64K for CachedWriter.      */
specifier|public
specifier|static
specifier|final
name|String
name|THRESHOLD_BUS_PROP
init|=
literal|"bus.io.CachedOutputStream.Threshold"
decl_stmt|;
comment|/**      * The data size in bytes to limit the maximum data size to be cached. No max size is set by default.      */
specifier|public
specifier|static
specifier|final
name|String
name|MAX_SIZE_SYS_PROP
init|=
literal|"org.apache.cxf.io.CachedOutputStream.MaxSize"
decl_stmt|;
comment|/**      * The data size in bytes to limit the maximum data size to be cached. No max size is set by default.      */
specifier|public
specifier|static
specifier|final
name|String
name|MAX_SIZE_BUS_PROP
init|=
literal|"bus.io.CachedOutputStream.MaxSize"
decl_stmt|;
comment|/**      * The cipher transformation name for encrypting the cached content. None is specified by default.      */
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_TRANSFORMATION_SYS_PROP
init|=
literal|"org.apache.cxf.io.CachedOutputStream.CipherTransformation"
decl_stmt|;
comment|/**      * The cipher transformation name for encrypting the cached content. None is specified by default.      */
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_TRANSFORMATION_BUS_PROP
init|=
literal|"bus.io.CachedOutputStream.CipherTransformation"
decl_stmt|;
specifier|private
name|CachedConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

