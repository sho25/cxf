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
name|token
operator|.
name|realm
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
comment|/**  * This class provides the functionality to match a given X509Certificate against a list of  * regular expressions.  */
end_comment

begin_class
specifier|public
class|class
name|CertConstraintsParser
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
name|CertConstraintsParser
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * a collection of compiled regular expression patterns for the subject DN      */
specifier|private
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|subjectDNPatterns
init|=
operator|new
name|ArrayList
argument_list|<
name|Pattern
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Set a list of Strings corresponding to regular expression constraints on the subject DN      * of a certificate      */
specifier|public
name|void
name|setSubjectConstraints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|constraints
parameter_list|)
block|{
if|if
condition|(
name|constraints
operator|!=
literal|null
condition|)
block|{
name|subjectDNPatterns
operator|=
operator|new
name|ArrayList
argument_list|<
name|Pattern
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|constraint
range|:
name|constraints
control|)
block|{
try|try
block|{
name|subjectDNPatterns
operator|.
name|add
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|constraint
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
specifier|public
name|Collection
argument_list|<
name|Pattern
argument_list|>
name|getCompiledSubjectContraints
parameter_list|()
block|{
return|return
name|subjectDNPatterns
return|;
block|}
comment|/**      * @return      true if the certificate's SubjectDN matches the constraints defined in the      *              subject DNConstraints; false, otherwise. The certificate subject DN only      *              has to match ONE of the subject cert constraints (not all).      */
specifier|public
name|boolean
name|matches
parameter_list|(
specifier|final
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
name|cert
parameter_list|)
block|{
if|if
condition|(
operator|!
name|subjectDNPatterns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The certificate is null so no constraints matching was possible"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|subjectName
init|=
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|subjectMatch
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Pattern
name|subjectDNPattern
range|:
name|subjectDNPatterns
control|)
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|subjectDNPattern
operator|.
name|matcher
argument_list|(
name|subjectName
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
literal|"Subject DN "
operator|+
name|subjectName
operator|+
literal|" matches with pattern "
operator|+
name|subjectDNPattern
argument_list|)
expr_stmt|;
name|subjectMatch
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|subjectMatch
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

