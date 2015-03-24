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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|ProcessedClaim
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

begin_comment
comment|/**  * This claim util class provides methods to make the handling of claims and claim values easier. The input  * claims (and their values) shall be treated as immutable. All util methods return a clone of the  * provided claim containing the desired claim update.  */
end_comment

begin_class
specifier|public
class|class
name|ClaimUtils
block|{
comment|/**      * @param collection Collection that should be used to add further claims to      * @param claims Claims to be added to the provided collection      * @return Returns clone of the provided collection including additional claims      */
specifier|public
name|ProcessedClaimCollection
name|add
parameter_list|(
name|ProcessedClaimCollection
name|collection
parameter_list|,
name|ProcessedClaim
modifier|...
name|claims
parameter_list|)
block|{
name|ProcessedClaimCollection
name|resultClaimCollection
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|collection
operator|!=
literal|null
condition|)
block|{
name|resultClaimCollection
operator|=
operator|(
name|ProcessedClaimCollection
operator|)
name|collection
operator|.
name|clone
argument_list|()
expr_stmt|;
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|claims
control|)
block|{
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|resultClaimCollection
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|resultClaimCollection
return|;
block|}
comment|/**      * @param collection Collection that should be used to add claims from the other provided claim      *            collections      * @param claimCollections All claims contained within the provided collections will be added to the      *            targetCollection      * @return Returns a clone of the provided collection containing all claims from all other claimCollections      */
specifier|public
name|ProcessedClaimCollection
name|add
parameter_list|(
name|ProcessedClaimCollection
name|collection
parameter_list|,
name|ProcessedClaimCollection
modifier|...
name|claimCollections
parameter_list|)
block|{
name|ProcessedClaimCollection
name|resultClaimCollection
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|collection
operator|!=
literal|null
condition|)
block|{
name|resultClaimCollection
operator|=
operator|(
name|ProcessedClaimCollection
operator|)
name|collection
operator|.
name|clone
argument_list|()
expr_stmt|;
for|for
control|(
name|ProcessedClaimCollection
name|cc
range|:
name|claimCollections
control|)
block|{
name|resultClaimCollection
operator|.
name|addAll
argument_list|(
name|cc
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaimCollection
return|;
block|}
comment|/**      * @param processedClaimTypeURI claim type URI      * @param values values of created claim. Can be null if no values shall be added to claim.      * @return Returns new claim with provided claim type and values      */
specifier|public
name|ProcessedClaim
name|create
parameter_list|(
name|String
name|processedClaimTypeURI
parameter_list|,
name|String
modifier|...
name|values
parameter_list|)
block|{
name|ProcessedClaim
name|processedClaim
init|=
operator|new
name|ProcessedClaim
argument_list|()
decl_stmt|;
if|if
condition|(
name|processedClaimTypeURI
operator|!=
literal|null
condition|)
block|{
name|processedClaim
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|processedClaimTypeURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
name|processedClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|processedClaim
return|;
block|}
comment|/**      * @param processedClaims Collection of multiple claims with different claim types      * @param processedClaimType URI of claim type to be selected from claim collection      * @return Returns first claim from claims collection matching the provided claimType      */
specifier|public
name|ProcessedClaim
name|get
parameter_list|(
name|ProcessedClaimCollection
name|processedClaims
parameter_list|,
name|String
name|processedClaimType
parameter_list|)
block|{
if|if
condition|(
name|processedClaimType
operator|==
literal|null
operator|||
name|processedClaims
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|processedClaims
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getClaimType
argument_list|()
operator|!=
literal|null
operator|&&
name|processedClaimType
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getClaimType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @param processedClaims Collection of claims to be mapped to a different claim type      * @param map Map of old:new claim types      * @param keepUnmapped if set to false only claims with a claim type contained in the map will be      *            returned. If set to false claims with an unmapped claim type will also be returned.      * @return Returns claim collection with mapped claim types      */
specifier|public
name|ProcessedClaimCollection
name|mapType
parameter_list|(
name|ProcessedClaimCollection
name|processedClaims
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|boolean
name|keepUnmapped
parameter_list|)
block|{
name|ProcessedClaimCollection
name|mappedProcessedClaims
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|processedClaims
operator|!=
literal|null
operator|&&
name|map
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|processedClaims
control|)
block|{
name|String
name|processedClaimType
init|=
operator|(
name|c
operator|.
name|getClaimType
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|c
operator|.
name|getClaimType
argument_list|()
operator|.
name|toString
argument_list|()
else|:
literal|""
decl_stmt|;
name|String
name|mappedProcessedClaimType
init|=
name|map
operator|.
name|get
argument_list|(
name|processedClaimType
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedProcessedClaimType
operator|!=
literal|null
condition|)
block|{
name|ProcessedClaim
name|processedClaim
init|=
name|c
operator|.
name|clone
argument_list|()
decl_stmt|;
name|processedClaim
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|mappedProcessedClaimType
argument_list|)
argument_list|)
expr_stmt|;
name|mappedProcessedClaims
operator|.
name|add
argument_list|(
name|processedClaim
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keepUnmapped
condition|)
block|{
name|mappedProcessedClaims
operator|.
name|add
argument_list|(
name|c
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|mappedProcessedClaims
return|;
block|}
comment|/**      * Mapping all values from the given claim according to the provided map. Input claims will not be      * modified. Result claim will be a clone of the provided claims just with different (mapped) claim      * values.      *       * @param processedClaim Claim providing values to be mapped      * @param map Map of old:new mapping values      * @param keepUnmapped if set to false only values contained in the map will be returned. If set to true,      *            values not contained in the map will also remain in the returned claim.      * @return Returns the provided claim with mapped values      */
specifier|public
name|ProcessedClaim
name|mapValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|mapping
parameter_list|,
name|boolean
name|keepUnmapped
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|mappedValues
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|mapping
operator|==
literal|null
operator|||
name|mapping
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|resultClaim
operator|.
name|setValues
argument_list|(
name|mappedValues
argument_list|)
expr_stmt|;
return|return
name|resultClaim
return|;
block|}
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|Object
name|newValue
init|=
name|mapping
operator|.
name|get
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|newValue
operator|!=
literal|null
condition|)
block|{
name|mappedValues
operator|.
name|add
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keepUnmapped
condition|)
block|{
name|mappedValues
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|resultClaim
operator|.
name|setValues
argument_list|(
name|mappedValues
argument_list|)
expr_stmt|;
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * Filtering all values from the given claim according to the provided regex filter. Input claims will not      * be modified. Result claim will be a clone of the provided claims just possible fewer (filtered) claim      * values.      *       * @param processedClaim Claim containing arbitrary values      * @param filter Regex filter to be used to match with claim values      * @return Returns a claim containing only values from the processedClaim which matched the provided      *         filter      */
specifier|public
name|ProcessedClaim
name|filterValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|filteredValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|filter
operator|==
literal|null
condition|)
block|{
name|resultClaim
operator|.
name|setValues
argument_list|(
name|filteredValues
argument_list|)
expr_stmt|;
return|return
name|resultClaim
return|;
block|}
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|toString
argument_list|()
operator|.
name|matches
argument_list|(
name|filter
argument_list|)
condition|)
block|{
name|filteredValues
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|resultClaim
operator|.
name|setValues
argument_list|(
name|filteredValues
argument_list|)
expr_stmt|;
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * Merges the first value (only) from different claim types in a collection to a new claim type separated      * by the provided delimiter.      *       * @param processedClaims Collection of claims containing claims with claim types of listed      *<code>claimType</code> array      * @param targetClaimType claim type URI of merged result claim      * @param delimiter Delimiter added between multiple claim types. Value can be<code>null</code>.      * @param processedClaimType URIs of claim types to be merged. Merging will be in the same order as the      *            provided claim type URIs. If a claim type is not found in the collection this claim type      *            will be omitted.      * @return Returns merged claim of all found claim types      */
specifier|public
name|ProcessedClaim
name|merge
parameter_list|(
name|ProcessedClaimCollection
name|processedClaims
parameter_list|,
name|String
name|targetClaimType
parameter_list|,
name|String
name|delimiter
parameter_list|,
name|String
modifier|...
name|processedClaimType
parameter_list|)
block|{
name|ProcessedClaim
name|mergedProcessedClaim
init|=
literal|null
decl_stmt|;
name|StringBuilder
name|sbProcessedClaimValue
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|sc
range|:
name|processedClaimType
control|)
block|{
name|ProcessedClaim
name|c
init|=
name|get
argument_list|(
name|processedClaims
argument_list|,
name|sc
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|c
operator|.
name|getValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|mergedProcessedClaim
operator|==
literal|null
condition|)
block|{
comment|// First match TODO refactor for better method override
name|mergedProcessedClaim
operator|=
name|c
operator|.
name|clone
argument_list|()
expr_stmt|;
name|sbProcessedClaimValue
operator|.
name|append
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|mergedProcessedClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|sbProcessedClaimValue
operator|.
name|append
argument_list|(
name|delimiter
argument_list|)
operator|.
name|append
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|mergedProcessedClaim
operator|!=
literal|null
condition|)
block|{
name|mergedProcessedClaim
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|targetClaimType
argument_list|)
argument_list|)
expr_stmt|;
name|mergedProcessedClaim
operator|.
name|addValue
argument_list|(
name|sbProcessedClaimValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mergedProcessedClaim
return|;
block|}
comment|/**      * @param processedClaim Claim to be updated      * @param processedClaimTypeURI URI as String to be set as claim type in provided claim      * @return Returns updated claim      */
specifier|public
name|ProcessedClaim
name|setType
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|String
name|processedClaimTypeURI
parameter_list|)
block|{
if|if
condition|(
name|processedClaim
operator|!=
literal|null
operator|&&
name|processedClaimTypeURI
operator|!=
literal|null
condition|)
block|{
name|processedClaim
operator|.
name|setClaimType
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|processedClaimTypeURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|processedClaim
return|;
block|}
comment|/**      * All claims within the provided collection will be updated in the following manner: If no original      * issuer is set, the issuer in the provided claims will be set as original issuer. If an original issuer      * was already set before, the original issuer will not be updated. All claims will be updated to have the      * provided issuer name be set as the claim issuer.      *       * @param processedClaims Collection of claims to be updated      * @param issuerName Issuer to be set for all claims within the collection      * @return Returns a new claim collection with clones of updated claims      */
specifier|public
name|ProcessedClaimCollection
name|updateIssuer
parameter_list|(
name|ProcessedClaimCollection
name|processedClaims
parameter_list|,
name|String
name|newIssuer
parameter_list|)
block|{
name|ProcessedClaimCollection
name|resultClaimCollection
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaims
operator|!=
literal|null
condition|)
block|{
name|resultClaimCollection
operator|=
operator|new
name|ProcessedClaimCollection
argument_list|()
expr_stmt|;
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|processedClaims
control|)
block|{
name|ProcessedClaim
name|newClaim
init|=
name|c
operator|.
name|clone
argument_list|()
decl_stmt|;
if|if
condition|(
name|newClaim
operator|.
name|getOriginalIssuer
argument_list|()
operator|==
literal|null
condition|)
block|{
name|newClaim
operator|.
name|setOriginalIssuer
argument_list|(
name|newClaim
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|newClaim
operator|.
name|setIssuer
argument_list|(
name|newIssuer
argument_list|)
expr_stmt|;
name|resultClaimCollection
operator|.
name|add
argument_list|(
name|newClaim
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaimCollection
return|;
block|}
comment|/**      * @param processedClaim values of this claim will be used for result claim      * @return Returns clone of the provided claim with values all in uppercase format      */
specifier|public
name|ProcessedClaim
name|upperCaseValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|newValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|oldValues
control|)
block|{
name|newValues
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|newValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * @param processedClaim values of this claim will be used for result claim      * @return Returns clone of provided claim with values all in lowercase format      */
specifier|public
name|ProcessedClaim
name|lowerCaseValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|newValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|oldValues
control|)
block|{
name|newValues
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|newValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * @param processedClaim Claim providing values to be wrapped      * @param prefix Prefix to be added to each claim value. Can be null.      * @param suffix Suffix to be appended to each claim value. Can be null.      * @return Returns a clone of the the provided claim with wrapped values      */
specifier|public
name|ProcessedClaim
name|wrapValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
name|prefix
operator|=
operator|(
name|prefix
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|prefix
expr_stmt|;
name|suffix
operator|=
operator|(
name|suffix
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|suffix
expr_stmt|;
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|newValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|oldValues
control|)
block|{
name|newValues
operator|.
name|add
argument_list|(
name|prefix
operator|+
name|value
operator|.
name|toString
argument_list|()
operator|+
name|suffix
argument_list|)
expr_stmt|;
block|}
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|newValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * This function is especially useful if multi values from a claim are stored within a single value entry.      * For example multi user roles could all be stored in a single value element separated by comma:      * USER,MANAGER,ADMIN The result of this function will provide a claim with three distinct values: USER      * and MANAGER and ADMIN.      *       * @param processedClaim claim containing multi-values in a single value entry      * @param delimiter Delimiter to split multi-values into single values      * @return Returns a clone of the provided claim containing only single values per value entry      */
specifier|public
name|ProcessedClaim
name|singleToMultiValue
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|newValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|oldValues
control|)
block|{
name|String
name|multivalue
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|multivalue
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|newValues
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|newValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * This function is especially useful if values from multiple claim values need to be condensed into a      * single value element. For example a user has three roles: USER and MANAGER and ADMIN. If ',' is used as      * a delimiter, then this method would provide the following claim with only a single value looking like      * this: USER,MANAGER,ADMIN      *       * @param processedClaim claim containing multi-values      * @param delimiter Delimiter to concatenate multi-values into a single value      * @return Returns a clone of the provided claim containing only one single value      */
specifier|public
name|ProcessedClaim
name|multiToSingleValue
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|oldValues
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|delimiter
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * This function removes duplicated values.      *       * @param processedClaim claim containing multi-values of which some might be duplicated      * @return Returns a clone of the provided claim containing only distinct values      */
specifier|public
name|ProcessedClaim
name|distinctValues
parameter_list|(
name|ProcessedClaim
name|processedClaim
parameter_list|)
block|{
name|ProcessedClaim
name|resultClaim
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaim
operator|!=
literal|null
condition|)
block|{
name|resultClaim
operator|=
name|processedClaim
operator|.
name|clone
argument_list|()
expr_stmt|;
if|if
condition|(
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|oldValues
init|=
name|resultClaim
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|distincValues
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|oldValues
argument_list|)
decl_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|resultClaim
operator|.
name|getValues
argument_list|()
operator|.
name|addAll
argument_list|(
name|distincValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|resultClaim
return|;
block|}
comment|/**      * Removes Claims without values.      *       * @param processedClaims Collection of claims with and/or without values      * @return Returns a collection of claims which contain values only      */
specifier|public
name|ProcessedClaimCollection
name|removeEmptyClaims
parameter_list|(
name|ProcessedClaimCollection
name|processedClaims
parameter_list|)
block|{
name|ProcessedClaimCollection
name|resultClaimCollection
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|processedClaims
operator|!=
literal|null
condition|)
block|{
name|resultClaimCollection
operator|=
operator|new
name|ProcessedClaimCollection
argument_list|()
expr_stmt|;
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|processedClaims
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
operator|&&
name|c
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|resultClaimCollection
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|resultClaimCollection
return|;
block|}
block|}
end_class

end_unit

