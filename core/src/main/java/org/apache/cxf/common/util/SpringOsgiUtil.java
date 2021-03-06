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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|ResourcePatternResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|io
operator|.
name|OsgiBundleResourcePatternResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|util
operator|.
name|BundleDelegatingClassLoader
import|;
end_import

begin_class
specifier|final
class|class
name|SpringOsgiUtil
block|{
specifier|private
name|SpringOsgiUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|ResourcePatternResolver
name|getResolver
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Bundle
name|bundle
decl_stmt|;
if|if
condition|(
name|loader
operator|instanceof
name|BundleDelegatingClassLoader
condition|)
block|{
name|bundle
operator|=
operator|(
operator|(
name|BundleDelegatingClassLoader
operator|)
name|loader
operator|)
operator|.
name|getBundle
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bundle
operator|=
name|FrameworkUtil
operator|.
name|getBundle
argument_list|(
name|SpringClasspathScanner
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|bundle
operator|!=
literal|null
condition|?
operator|new
name|OsgiBundleResourcePatternResolver
argument_list|(
name|bundle
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

