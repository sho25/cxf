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
name|sts
operator|.
name|claims
operator|.
name|mapper
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Map
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
name|commons
operator|.
name|jexl2
operator|.
name|JexlContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jexl2
operator|.
name|JexlEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jexl2
operator|.
name|MapContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|jexl2
operator|.
name|Script
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
name|sts
operator|.
name|claims
operator|.
name|ClaimsMapper
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
name|sts
operator|.
name|claims
operator|.
name|ClaimsParameters
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
name|sts
operator|.
name|claims
operator|.
name|ProcessedClaimCollection
import|;
end_import

begin_class
specifier|public
class|class
name|JexlClaimsMapper
implements|implements
name|ClaimsMapper
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
name|JexlClaimsMapper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JexlEngine
name|jexlEngine
init|=
operator|new
name|JexlEngine
argument_list|()
decl_stmt|;
specifier|private
name|Script
name|script
decl_stmt|;
specifier|public
name|JexlClaimsMapper
parameter_list|()
block|{
comment|// jexl.setCache(512);
comment|// jexl.setLenient(false);
name|jexlEngine
operator|.
name|setSilent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|functions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|functions
operator|.
name|put
argument_list|(
literal|"claims"
argument_list|,
operator|new
name|ClaimUtils
argument_list|()
argument_list|)
expr_stmt|;
name|jexlEngine
operator|.
name|setFunctions
argument_list|(
name|functions
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JexlClaimsMapper
parameter_list|(
name|String
name|script
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
if|if
condition|(
name|script
operator|!=
literal|null
condition|)
block|{
name|setScript
argument_list|(
name|script
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ProcessedClaimCollection
name|mapClaims
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|ProcessedClaimCollection
name|sourceClaims
parameter_list|,
name|String
name|targetRealm
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
name|JexlContext
name|context
init|=
operator|new
name|MapContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|set
argument_list|(
literal|"sourceClaims"
argument_list|,
name|sourceClaims
argument_list|)
expr_stmt|;
name|context
operator|.
name|set
argument_list|(
literal|"targetClaims"
argument_list|,
operator|new
name|ProcessedClaimCollection
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|set
argument_list|(
literal|"sourceRealm"
argument_list|,
name|sourceRealm
argument_list|)
expr_stmt|;
name|context
operator|.
name|set
argument_list|(
literal|"targetRealm"
argument_list|,
name|targetRealm
argument_list|)
expr_stmt|;
name|context
operator|.
name|set
argument_list|(
literal|"claimsParameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|Script
name|s
init|=
name|getScript
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No claim mapping script defined"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ProcessedClaimCollection
argument_list|()
return|;
comment|// TODO Check if null or an exception would be more
comment|// appropriate
block|}
else|else
block|{
return|return
operator|(
name|ProcessedClaimCollection
operator|)
name|s
operator|.
name|execute
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
specifier|public
name|Script
name|getScript
parameter_list|()
block|{
return|return
name|script
return|;
block|}
specifier|public
name|void
name|setScript
parameter_list|(
name|Script
name|script
parameter_list|)
block|{
name|this
operator|.
name|script
operator|=
name|script
expr_stmt|;
block|}
specifier|public
name|void
name|setScript
parameter_list|(
name|String
name|scriptLocation
parameter_list|)
throws|throws
name|IOException
block|{
name|URL
name|resource
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|scriptLocation
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Script resource not found!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|script
operator|=
name|jexlEngine
operator|.
name|createScript
argument_list|(
operator|new
name|File
argument_list|(
name|resource
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JexlEngine
name|getJexlEngine
parameter_list|()
block|{
return|return
name|jexlEngine
return|;
block|}
specifier|public
name|void
name|setJexlEngine
parameter_list|(
name|JexlEngine
name|jexl
parameter_list|)
block|{
name|this
operator|.
name|jexlEngine
operator|=
name|jexl
expr_stmt|;
block|}
block|}
end_class

end_unit

