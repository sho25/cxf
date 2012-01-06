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
name|service
package|;
end_package

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
name|Collection
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
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

begin_comment
comment|/**  * This class represents a (static) service. It can be spring-loaded with a set of Endpoint  * Strings, which are compiled into a collection of (reg-ex) Patterns.  */
end_comment

begin_class
specifier|public
class|class
name|StaticService
implements|implements
name|ServiceMBean
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
name|StaticService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|tokenType
decl_stmt|;
specifier|private
name|String
name|keyType
decl_stmt|;
specifier|private
name|EncryptionProperties
name|encryptionProperties
decl_stmt|;
comment|/**      * a collection of compiled regular expression patterns      */
specifier|private
specifier|final
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|endpointPatterns
init|=
operator|new
name|ArrayList
argument_list|<
name|Pattern
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Return true if the supplied address corresponds to a known address for this service      */
specifier|public
name|boolean
name|isAddressInEndpoints
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|String
name|addressToMatch
init|=
name|address
decl_stmt|;
if|if
condition|(
name|addressToMatch
operator|==
literal|null
condition|)
block|{
name|addressToMatch
operator|=
literal|""
expr_stmt|;
block|}
for|for
control|(
name|Pattern
name|endpointPattern
range|:
name|endpointPatterns
control|)
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|endpointPattern
operator|.
name|matcher
argument_list|(
name|addressToMatch
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Address "
operator|+
name|address
operator|+
literal|" matches with pattern "
operator|+
name|endpointPattern
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Get the default Token Type to be issued for this Service      */
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
comment|/**      * Set the default Token Type to be issued for this Service      */
specifier|public
name|void
name|setTokenType
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|tokenType
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting Token Type: "
operator|+
name|tokenType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default Key Type to be issued for this Service      */
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
comment|/**      * Set the default Key Type to be issued for this Service      */
specifier|public
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting Key Type: "
operator|+
name|keyType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the list of endpoint addresses that correspond to this service      */
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|endpoints
parameter_list|)
block|{
if|if
condition|(
name|endpoints
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|endpoint
range|:
name|endpoints
control|)
block|{
try|try
block|{
name|endpointPatterns
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|endpoint
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Get the EncryptionProperties to be used to encrypt tokens issued for this service      */
specifier|public
name|EncryptionProperties
name|getEncryptionProperties
parameter_list|()
block|{
return|return
name|encryptionProperties
return|;
block|}
comment|/**      * Set the EncryptionProperties to be used to encrypt tokens issued for this service      */
specifier|public
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|)
block|{
name|this
operator|.
name|encryptionProperties
operator|=
name|encryptionProperties
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting encryption properties"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

