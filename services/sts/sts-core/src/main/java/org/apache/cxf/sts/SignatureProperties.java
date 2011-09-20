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
package|;
end_package

begin_comment
comment|/**  * This class contains various configuration properties that can be used to sign an issued token.  */
end_comment

begin_class
specifier|public
class|class
name|SignatureProperties
block|{
specifier|private
name|boolean
name|useKeyValue
decl_stmt|;
specifier|private
name|long
name|keySize
init|=
literal|256
decl_stmt|;
specifier|private
name|long
name|minimumKeySize
init|=
literal|128
decl_stmt|;
specifier|private
name|long
name|maximumKeySize
init|=
literal|512
decl_stmt|;
comment|/**      * Get whether a KeyValue is used to refer to a a certificate used to sign an issued token.       * The default is false.      */
specifier|public
name|boolean
name|isUseKeyValue
parameter_list|()
block|{
return|return
name|useKeyValue
return|;
block|}
comment|/**      * Set whether a KeyValue is used to refer to a a certificate used to sign an issued token.       * The default is false.      */
specifier|public
name|void
name|setUseKeyValue
parameter_list|(
name|boolean
name|useKeyValue
parameter_list|)
block|{
name|this
operator|.
name|useKeyValue
operator|=
name|useKeyValue
expr_stmt|;
block|}
comment|/**      * Get the key size to use when generating a symmetric key to sign an issued token. The default is      * 256 bits.      */
specifier|public
name|long
name|getKeySize
parameter_list|()
block|{
return|return
name|keySize
return|;
block|}
comment|/**      * Set the key size to use when generating a symmetric key to sign an issued token. The default is      * 256 bits.      */
specifier|public
name|void
name|setKeySize
parameter_list|(
name|long
name|keySize
parameter_list|)
block|{
name|this
operator|.
name|keySize
operator|=
name|keySize
expr_stmt|;
block|}
comment|/**      * Get the minimum key size to use when generating a symmetric key to sign an issued token. The      * requestor can specify a KeySize value to use. The default is 128 bits.      */
specifier|public
name|long
name|getMinimumKeySize
parameter_list|()
block|{
return|return
name|minimumKeySize
return|;
block|}
comment|/**      * Set the minimum key size to use when generating a symmetric key to sign an issued token. The      * requestor can specify a KeySize value to use. The default is 128 bits.      */
specifier|public
name|void
name|setMinimumKeySize
parameter_list|(
name|long
name|minimumKeySize
parameter_list|)
block|{
name|this
operator|.
name|minimumKeySize
operator|=
name|minimumKeySize
expr_stmt|;
block|}
comment|/**      * Get the maximum key size to use when generating a symmetric key to sign an issued token. The      * requestor can specify a KeySize value to use. The default is 512 bits.      */
specifier|public
name|long
name|getMaximumKeySize
parameter_list|()
block|{
return|return
name|maximumKeySize
return|;
block|}
comment|/**      * Set the maximum key size to use when generating a symmetric key to sign an issued token. The      * requestor can specify a KeySize value to use. The default is 512 bits.      */
specifier|public
name|void
name|setMaximumKeySize
parameter_list|(
name|long
name|maximumKeySize
parameter_list|)
block|{
name|this
operator|.
name|maximumKeySize
operator|=
name|maximumKeySize
expr_stmt|;
block|}
block|}
end_class

end_unit

